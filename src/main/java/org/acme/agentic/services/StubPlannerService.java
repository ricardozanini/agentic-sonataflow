package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.ItineraryItem;
import org.acme.agentic.model.ItineraryRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StubPlannerService implements PlannerService {

    @Inject
    StubUtils utils;

    @Tool("build an itinerary based on the city, interests, and number of days")
    @Override
    public List<ItineraryItem> buildItinerary(ItineraryRequest request) {
       return utils.loadList("itinerary.json", new TypeReference<>() {
        });
    }
}
