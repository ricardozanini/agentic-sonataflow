package org.acme.agentic.services;

import java.util.List;

import org.acme.agentic.model.Flight;
import org.acme.agentic.model.FlightRequest;

public interface FlightService {
    List<Flight> searchFlights(FlightRequest request);
}
