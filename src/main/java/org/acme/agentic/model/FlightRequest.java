package org.acme.agentic.model;

import java.time.LocalDate;

public class FlightRequest {
    private String destination;
    private LocalDate date;

    public FlightRequest() {}

    public FlightRequest(String destination, LocalDate date) {
        this.destination = destination;
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
