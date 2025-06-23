package org.acme.agentic.model;

import java.time.LocalDateTime;

public class Flight {
    private String airline;
    private int price;
    private LocalDateTime departure;

    public Flight() {}

    public Flight(String airline, int price, LocalDateTime departure) {
        this.airline = airline;
        this.price = price;
        this.departure = departure;
    }

    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public LocalDateTime getDeparture() { return departure; }
    public void setDeparture(LocalDateTime departure) { this.departure = departure; }
}
