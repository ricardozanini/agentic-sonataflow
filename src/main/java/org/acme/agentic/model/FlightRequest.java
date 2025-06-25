package org.acme.agentic.model;

import java.time.LocalDate;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightRequest that = (FlightRequest) o;
        return Objects.equals(destination, that.destination) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, date);
    }

    @Override
    public String toString() {
        return "FlightRequest{" +
                "destination='" + destination + '\'' +
                ", date=" + date +
                '}';
    }
}
