package org.acme.agentic.workflows;

import org.acme.agentic.agents.TravelPlannerAgent;
import org.kie.kogito.serverless.workflow.fluent.FunctionBuilder;

import io.serverlessworkflow.api.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static org.kie.kogito.serverless.workflow.fluent.ActionBuilder.call;
import static org.kie.kogito.serverless.workflow.fluent.StateBuilder.operation;
import static org.kie.kogito.serverless.workflow.fluent.WorkflowBuilder.workflow;

@ApplicationScoped
public class TravelPlannerFlow {

    @Inject
    TravelPlannerAgent travelPlannerAgent;

    public Workflow flightPriceWatcher() {
        return workflow("flight-price-watcher")
                .start(operation()
                        .action(call(FunctionBuilder.java("planTrip", travelPlannerAgent::planTrip), ".req")).outputFilter(".response"))
                .end().build();
    }
}
