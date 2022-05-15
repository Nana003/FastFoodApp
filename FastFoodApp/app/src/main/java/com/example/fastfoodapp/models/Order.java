package com.example.fastfoodapp.models;

import java.util.ArrayList;
import java.util.List;

public class Order {
    String total;
    Long id;
    String imageUrl;

    public Order(String total, Long id, String imageUrl) {
        this.total = total;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public Order() {
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Order{" +
                "total='" + total + '\'' +
                ", id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
