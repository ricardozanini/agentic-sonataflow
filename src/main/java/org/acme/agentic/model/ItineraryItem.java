package org.acme.agentic.model;

import java.util.List;
import java.util.Objects;

public class ItineraryItem {
    private int day;
    private List<String> activities;

    public ItineraryItem() {
    }

    public ItineraryItem(int day, List<String> activities) {
        this.day = day;
        this.activities = activities;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "ItineraryItem{" +
                "day=" + day +
                ", activities=" + activities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItineraryItem that = (ItineraryItem) o;
        return day == that.day && Objects.equals(activities, that.activities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, activities);
    }
}
