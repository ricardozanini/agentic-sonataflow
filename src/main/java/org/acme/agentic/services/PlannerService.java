package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.ItineraryItem;
import org.acme.agentic.model.ItineraryRequest;

public interface PlannerService {
    List<ItineraryItem> buildItinerary(ItineraryRequest request);
}
