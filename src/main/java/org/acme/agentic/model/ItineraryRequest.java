package org.acme.agentic.model;

import java.util.List;

public class ItineraryRequest {
    private String city;
    private List<String> interests;
    private int days;

    public ItineraryRequest() {}

    public ItineraryRequest(String city, List<String> interests, int days) {
        this.city = city;
        this.interests = interests;
        this.days = days;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
