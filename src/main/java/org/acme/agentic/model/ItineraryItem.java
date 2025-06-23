package org.acme.agentic.model;

import java.util.List;

public class ItineraryItem {
    private int day;
    private List<String> activities;

    public ItineraryItem() {}

    public ItineraryItem(int day, List<String> activities) {
        this.day = day;
        this.activities = activities;
    }

    public int getDay() { return day; }
    public void setDay(int day) { this.day = day; }

    public List<String> getActivities() { return activities; }
    public void setActivities(List<String> activities) { this.activities = activities; }
}
