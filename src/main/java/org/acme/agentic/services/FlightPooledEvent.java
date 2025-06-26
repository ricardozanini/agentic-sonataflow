package org.acme.agentic.services;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.acme.agentic.model.Flight;
import org.acme.agentic.model.FlightRequest;
import org.acme.agentic.workflows.TravelPlannerFlow;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.jackson.JsonCloudEventData;

public class FlightPooledEvent {
    private FlightRequest request;
    private double budget;
    private Optional<Flight> bestFlight;

    public FlightPooledEvent(FlightRequest request, double budget, Optional<Flight> bestFlight) {
        this.request = request;
        this.bestFlight = bestFlight;
        this.budget = budget;
    }

    public FlightPooledEvent() {
    }

    public FlightRequest getRequest() {
        return request;
    }

    public void setRequest(FlightRequest request) {
        this.request = request;
    }

    public Optional<Flight> getBestFlight() {
        return bestFlight;
    }

    public void setBestFlight(Optional<Flight> bestFlight) {
        this.bestFlight = bestFlight;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public CloudEvent asCloudEvent(final String processId, ObjectMapper objectMapper) {
        return CloudEventBuilder.v1().withId(UUID.randomUUID().toString())
                .withSource(URI.create(""))
                .withType(TravelPlannerFlow.Events.FLIGHT_POOLER_RESULT)
                .withTime(OffsetDateTime.now())
                .withExtension("kogitoprocrefid", processId)
                .withData(JsonCloudEventData.wrap(objectMapper.valueToTree(this)))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightPooledEvent that = (FlightPooledEvent) o;
        return Double.compare(budget, that.budget) == 0 && Objects.equals(request, that.request) && Objects.equals(bestFlight, that.bestFlight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request, budget, bestFlight);
    }

    @Override
    public String toString() {
        return "FlightPooledEvent{" + "request=" + request + ", budget=" + budget + ", bestFlight=" + bestFlight + '}';
    }
}
