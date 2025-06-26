package org.acme.agentic;

import java.util.Map;

import org.acme.agentic.workflows.TravelPlannerFlow;
import org.kie.kogito.serverless.workflow.executor.StaticWorkflowApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/plan")
public class TravelPlannerResource {
    private static final Logger LOG = LoggerFactory.getLogger(TravelPlannerResource.class);

    @Inject
    TravelPlannerFlow travelPlannerFlow;

    @POST
    public Response planTravel(String travelRequirement) {
        try (StaticWorkflowApplication app = StaticWorkflowApplication.create()) {
            var workflowId = app.execute(travelPlannerFlow.flightPriceWatcherFlow(), Map.of("req", travelRequirement)).getId();
            LOG.info("Workflow has been started with ID: {}", workflowId);
            return Response.ok(workflowId).type(MediaType.TEXT_PLAIN).build();
        }
    }

}
