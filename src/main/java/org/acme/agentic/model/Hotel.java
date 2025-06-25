package org.acme.agentic.model;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return price == hotel.price && Double.compare(rating, hotel.rating) == 0 && Objects.equals(name, hotel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, rating);
    }
}
