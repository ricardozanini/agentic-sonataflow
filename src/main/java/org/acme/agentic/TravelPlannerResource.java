package org.acme.agentic;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.acme.agentic.workflows.TravelPlannerFlow;
import org.kie.kogito.serverless.workflow.executor.StaticWorkflowApplication;
import org.kie.kogito.serverless.workflow.models.JsonNodeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

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
            JsonNodeModel processInstance = app.execute(travelPlannerFlow.flightPriceWatcherFlow(), Map.of("req", travelRequirement));
            JsonNode workflowData = processInstance.getWorkflowdata();
            String workflowId = processInstance.getId();
            if (app.findProcessById(workflowId).isPresent()) {
                LOG.info("Workflow has been started with ID: {}", workflowId);
                workflowData = app.waitForFinish(workflowId, Duration.ofSeconds(90000)).orElseThrow().getWorkflowdata();
            }
            LOG.info("Workflow data is: \n {}", workflowData);
            return Response.ok(workflowData).type(MediaType.TEXT_PLAIN).build();
          }
          catch (InterruptedException | TimeoutException e) {
            LOG.error("Workflow has been interrupted", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
