package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.Flight;
import org.acme.agentic.model.FlightRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class StubFlightService implements FlightService {

    @Inject
    StubUtils utils;

    @Tool("search for flights based on destination and date")
    @Override
    public List<Flight> searchFlights(FlightRequest request) {
        return utils.loadList("flights.json", new TypeReference<>() {
        });
    }
}
