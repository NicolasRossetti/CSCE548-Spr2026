package com.bazaar.model;

import java.time.LocalDateTime;

public class Order {
    private int orderId;
    private int itemId;
    private String orderType;
    private int quantity;
    private double targetPrice;
    private String status;
    private LocalDateTime createdAt;

    public Order() {
    }

    public Order(int orderId, int itemId, String orderType, int quantity,
                 double targetPrice, String status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.orderType = orderType;
        this.quantity = quantity;
        this.targetPrice = targetPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%d, itemId=%d, type=%s, qty=%d, targetPrice=%.2f, status=%s, created=%s]",
                orderId, itemId, orderType, quantity, targetPrice, status, createdAt);
    }
}
