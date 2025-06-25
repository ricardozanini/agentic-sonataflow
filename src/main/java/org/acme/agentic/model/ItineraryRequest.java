package org.acme.agentic.model;

import java.util.List;
import java.util.Objects;

public class ItineraryRequest {
    private String city;
    private List<String> interests;
    private int days;

    public ItineraryRequest() {
    }

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

    @Override
    public String toString() {
        return "ItineraryRequest{" +
                "city='" + city + '\'' +
                ", interests=" + interests +
                ", days=" + days +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItineraryRequest that = (ItineraryRequest) o;
        return days == that.days && Objects.equals(city, that.city) && Objects.equals(interests, that.interests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, interests, days);
    }
}
