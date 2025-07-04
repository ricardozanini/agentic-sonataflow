package org.acme.agentic.workflows;

import org.acme.agentic.agents.PlannerSummaryAgent;
import org.acme.agentic.agents.TravelPlannerAgent;
import org.acme.agentic.services.BudgetFlightPooler;
import org.acme.agentic.services.NotificationService;
import org.kie.kogito.serverless.workflow.actions.WorkflowLogLevel;
import org.kie.kogito.serverless.workflow.fluent.FunctionBuilder;
import org.kie.kogito.serverless.workflow.fluent.OperationStateBuilder;

import io.serverlessworkflow.api.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static org.kie.kogito.serverless.workflow.fluent.ActionBuilder.call;
import static org.kie.kogito.serverless.workflow.fluent.ActionBuilder.log;
import static org.kie.kogito.serverless.workflow.fluent.EventDefBuilder.eventDef;
import static org.kie.kogito.serverless.workflow.fluent.StateBuilder.callback;
import static org.kie.kogito.serverless.workflow.fluent.StateBuilder.operation;
import static org.kie.kogito.serverless.workflow.fluent.WorkflowBuilder.workflow;

@ApplicationScoped
public class TravelPlannerFlow {

    @Inject
    TravelPlannerAgent travelPlannerAgent;

    @Inject
    PlannerSummaryAgent plannerSummaryAgent;

    @Inject
    NotificationService notificationService;

    @Inject
    BudgetFlightPooler budgetFlightPooler;

    public Workflow flightPriceWatcherFlow() {
        final OperationStateBuilder callPlannerSummaryAgent = operation()
                .action(call("summaryPlan", "{ plan, airfareValue, airfareBudget, userAddress }"))
                .outputFilter(".response");

        return workflow("flight-price-watcher")
                .function(FunctionBuilder.java("summaryPlan", plannerSummaryAgent::summaryPlan))
                .start(operation()
                        // returns a schema as { plan, airfareValue, airfareBudget }
                        .action(
                                call(FunctionBuilder.java("planTrip", travelPlannerAgent::planTrip), ".req").outputFilter("{ plan, airfareValue, airfareBudget, userAddress, flightRequest }")))
                .when(".airfareBudget == 0 or .airfareValue <= .airfareBudget")
                    .next(callPlannerSummaryAgent).end()
                .when(".airfareValue > .airfareBudget")
                    .next(operation()
                            .action(
                                    call(FunctionBuilder.java("notifyPooling", notificationService::notifyPooling),
                                            ".userAddress", "Travel Plan On Holding", "Your travel plan is on holding, we will check with Flight companies a better price under your budget.").noResult()))
                    .next(callback(
                            call(FunctionBuilder.java("findCheaperFlightAsync", budgetFlightPooler::findCheaperFlightAsync),
                                    ".flightRequest", "{ budget: .airfareBudget, attempts: 10, intervalMs: 1000 }", "$WORKFLOW.instanceId").noResult(), eventDef(Events.FLIGHT_POOLER_RESULT)))
                    .next(operation().action(log(WorkflowLogLevel.INFO, " . ")))
                    .next(callPlannerSummaryAgent).end()
                .end().build();
    }

    public static final class Events {
        public static final String FLIGHT_POOLER_RESULT = "org.acme.agentic.flightPoolerResult";
    }
}
