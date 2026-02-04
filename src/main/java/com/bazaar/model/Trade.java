package com.bazaar.model;

import java.time.LocalDateTime;

public class Trade {
    private int tradeId;
    private int orderId;
    private int qtyFilled;
    private double fillPrice;
    private double fee;
    private double profit;
    private LocalDateTime tradeTime;

    public Trade() {
    }

    public Trade(int tradeId, int orderId, int qtyFilled, double fillPrice,
                 double fee, double profit, LocalDateTime tradeTime) {
        this.tradeId = tradeId;
        this.orderId = orderId;
        this.qtyFilled = qtyFilled;
        this.fillPrice = fillPrice;
        this.fee = fee;
        this.profit = profit;
        this.tradeTime = tradeTime;
    }

    // Getters and Setters
    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getQtyFilled() {
        return qtyFilled;
    }

    public void setQtyFilled(int qtyFilled) {
        this.qtyFilled = qtyFilled;
    }

    public double getFillPrice() {
        return fillPrice;
    }

    public void setFillPrice(double fillPrice) {
        this.fillPrice = fillPrice;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public LocalDateTime getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(LocalDateTime tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Override
    public String toString() {
        return String.format("Trade[id=%d, orderId=%d, qtyFilled=%d, fillPrice=%.2f, fee=%.2f, profit=%.2f, time=%s]",
                tradeId, orderId, qtyFilled, fillPrice, fee, profit, tradeTime);
    }
}
