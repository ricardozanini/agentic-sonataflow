package org.acme.agentic.workflows;

import org.acme.agentic.agents.TravelPlannerAgent;
import org.acme.agentic.services.BudgetFlightPooler;
import org.acme.agentic.services.FlightPooledEvent;
import org.acme.agentic.services.NotificationService;
import org.kie.kogito.serverless.workflow.actions.WorkflowLogLevel;
import org.kie.kogito.serverless.workflow.fluent.FunctionBuilder;
import org.kie.kogito.serverless.workflow.parser.types.SysOutTypeHandler;

import io.serverlessworkflow.api.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import static org.kie.kogito.serverless.workflow.fluent.ActionBuilder.call;
import static org.kie.kogito.serverless.workflow.fluent.EventDefBuilder.eventDef;
import static org.kie.kogito.serverless.workflow.fluent.FunctionBuilder.log;
import static org.kie.kogito.serverless.workflow.fluent.StateBuilder.callback;
import static org.kie.kogito.serverless.workflow.fluent.StateBuilder.operation;
import static org.kie.kogito.serverless.workflow.fluent.WorkflowBuilder.jsonObject;
import static org.kie.kogito.serverless.workflow.fluent.WorkflowBuilder.workflow;

@ApplicationScoped
public class TravelPlannerFlow {

    public static final class Events {
        public static final String FLIGHT_POOLER_RESULT = "org.acme.agentic.flightPoolerResult";
    }

    @Inject
    TravelPlannerAgent travelPlannerAgent;

    @Inject
    NotificationService notificationService;

    @Inject
    BudgetFlightPooler budgetFlightPooler;

    public void onBudgetFlightPoolerResult(@Observes FlightPooledEvent e) {

    }


    public Workflow flightPriceWatcherFlow() {
        return workflow("flight-price-watcher")
                .start(operation()
                        // returns a schema as { plan, airfareValue, airfareBudget }
                        .action(call(FunctionBuilder.java("planTrip", travelPlannerAgent::planTrip), ".req")).outputFilter("{ plan, airfareValue, airfareBudget, userAddress, flightRequest }"))
                .when(".airfareBudget == 0 or .airfareValue <= .airfareBudget")
                    .next(operation().action(call(log("logLowerBudget", WorkflowLogLevel.INFO), jsonObject().put(SysOutTypeHandler.SYSOUT_TYPE_PARAM, "Airfare Value is bellow the budget, we are ok")))).end()
                .when(".airfareValue > .airfareBudget")
                    .next(operation().action(call(log("logHigherBudget", WorkflowLogLevel.INFO), jsonObject().put(SysOutTypeHandler.SYSOUT_TYPE_PARAM, "Airfare Value is higher then the budget, let's start pooling for airfare prices"))))
                    // We could have Quarkus Qute service to handle email body or call another agent to write the email for us
                    // Alternatively, the workflow could also send a Kafka/AMQ/CloudEvent message
                    .next(operation().outputFilter("del(.response)").action(call(FunctionBuilder.java("notifyPooling", notificationService::notifyPooling), ".userAddress", "Your travel plan is on holding, we will check with Flight companies a better price under your budget.")))
                    .next(callback(call(FunctionBuilder.java("findCheaperFlightAsync", budgetFlightPooler::findCheaperFlightAsync), ".flightRequest", "{ budget: .airfareBudget, attempts: 10, intervalMs: 1000 }"), eventDef(Events.FLIGHT_POOLER_RESULT))).end()
                .end().build();
    }
}
