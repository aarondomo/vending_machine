package com.aarondomo.vendingmachine.model;

public class Product {

    private String name;
    private int price;
    private int picture;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, int price, int picture) {
        this.name = name;
        this.price = price;
        this.picture = picture;

    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
