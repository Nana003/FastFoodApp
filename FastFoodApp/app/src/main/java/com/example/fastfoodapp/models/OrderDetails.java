package com.example.fastfoodapp.models;

public class OrderDetails {
    String name;
    String imgUrl;
    double price;
    int quantity;
    String totalPriceItem;

    public OrderDetails(String name, String imgUrl, double price, int quantity, String totalPriceItem) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.price = price;
        this.quantity = quantity;
        this.totalPriceItem = totalPriceItem;
    }

    public OrderDetails() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPriceItem() {
        return totalPriceItem;
    }

    public void setTotalPriceItem(String totalPriceItem) {
        this.totalPriceItem = totalPriceItem;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPriceItem='" + totalPriceItem + '\'' +
                '}';
    }
}
