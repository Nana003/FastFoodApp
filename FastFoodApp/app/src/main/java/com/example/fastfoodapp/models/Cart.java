package com.example.fastfoodapp.models;

public class Cart {
    String name;
    double price;
    String imgUrl;
    int quantity;

    public Cart(String name, double price, String imageUrl, int quantity) {
        this.name = name;
        this.price = price;
        this.imgUrl = imageUrl;
        this.quantity = quantity;
    }

    public Cart() {
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
