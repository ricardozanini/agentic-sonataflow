package org.acme.agentic.model;

public class HotelRequest {
    private String location;
    private int maxPricePerNight;
    private int nights;

    public HotelRequest() {}

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
}
