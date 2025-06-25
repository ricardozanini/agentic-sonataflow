package org.acme.agentic.model;

import java.util.Objects;

public class HotelRequest {
    private String location;
    private int maxPricePerNight;
    private int nights;

    public HotelRequest() {
    }

    public HotelRequest(String location, int maxPricePerNight, int nights) {
        this.location = location;
        this.maxPricePerNight = maxPricePerNight;
        this.nights = nights;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxPricePerNight() {
        return maxPricePerNight;
    }

    public void setMaxPricePerNight(int maxPricePerNight) {
        this.maxPricePerNight = maxPricePerNight;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HotelRequest that = (HotelRequest) o;
        return maxPricePerNight == that.maxPricePerNight && nights == that.nights && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, maxPricePerNight, nights);
    }

    @Override
    public String toString() {
        return "HotelRequest{" +
                "location='" + location + '\'' +
                ", maxPricePerNight=" + maxPricePerNight +
                ", nights=" + nights +
                '}';
    }
}
