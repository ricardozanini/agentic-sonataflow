package org.acme.agentic;

import org.acme.agentic.agents.TravelPlannerAgent;
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

    //    @Inject
//    TravelPlannerFlow travelPlannerFlow;
    @Inject
    TravelPlannerAgent agent;

    @POST
    public Response planTravel(String travelRequirement) {
//        try (StaticWorkflowApplication app = StaticWorkflowApplication.create()) {
//            var flowData = app.execute(travelPlannerFlow.flightPriceWatcher(), Map.of("req", travelRequirement)).getWorkflowdata().toPrettyString();
//            LOG.info("Response returned from workflow: \n{}", flowData);
//            return Response.ok(flowData).type(MediaType.APPLICATION_JSON).build();
//        }
        var response = agent.planTrip(travelRequirement);
        LOG.info("Response returned from workflow: \n{}", response);
        return Response.ok(response).type(MediaType.APPLICATION_JSON).build();
    }

}
