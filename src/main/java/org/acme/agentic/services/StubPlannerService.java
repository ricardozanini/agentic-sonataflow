package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.ItineraryItem;
import org.acme.agentic.model.ItineraryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StubPlannerService implements PlannerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubPlannerService.class);

    @Inject
    StubUtils utils;

    @Tool("build an itinerary based on the city, interests, and number of days")
    @Override
    public List<ItineraryItem> buildItinerary(ItineraryRequest request) {
        LOGGER.info("Building itinerary based on itinerary request {}", request);
       return utils.loadList("itinerary.json", new TypeReference<>() {
        });
    }
}
