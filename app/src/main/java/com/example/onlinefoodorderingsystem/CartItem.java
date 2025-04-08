package com.example.onlinefoodorderingsystem;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String itemName;
    private double itemPrice;
    private int quantity;

    public CartItem(String itemName, double itemPrice, int quantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Override toString() for easy debugging/logging
    @Override
    public String toString() {
        return "CartItem{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", quantity=" + quantity +
                '}';
    }
}
