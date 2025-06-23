package org.acme.agentic.model;

public class Hotel {
    private String name;
    private int price;
    private double rating;

    public Hotel() {}

    public Hotel(String name, int price, double rating) {
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
