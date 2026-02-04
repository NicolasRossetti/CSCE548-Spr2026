package com.bazaar.model;

import java.time.LocalDateTime;

public class PriceSnapshot {
    private int snapshotId;
    private int itemId;
    private double buyPrice;
    private double sellPrice;
    private int buyVolume;
    private int sellVolume;
    private LocalDateTime snapshotTime;

    public PriceSnapshot() {
    }

    public PriceSnapshot(int snapshotId, int itemId, double buyPrice, double sellPrice,
                         int buyVolume, int sellVolume, LocalDateTime snapshotTime) {
        this.snapshotId = snapshotId;
        this.itemId = itemId;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.buyVolume = buyVolume;
        this.sellVolume = sellVolume;
        this.snapshotTime = snapshotTime;
    }

    // Getters and Setters
    public int getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(int snapshotId) {
        this.snapshotId = snapshotId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getBuyVolume() {
        return buyVolume;
    }

    public void setBuyVolume(int buyVolume) {
        this.buyVolume = buyVolume;
    }

    public int getSellVolume() {
        return sellVolume;
    }

    public void setSellVolume(int sellVolume) {
        this.sellVolume = sellVolume;
    }

    public LocalDateTime getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(LocalDateTime snapshotTime) {
        this.snapshotTime = snapshotTime;
    }

    @Override
    public String toString() {
        return String.format("PriceSnapshot[id=%d, itemId=%d, buy=%.2f, sell=%.2f, buyVol=%d, sellVol=%d, time=%s]",
                snapshotId, itemId, buyPrice, sellPrice, buyVolume, sellVolume, snapshotTime);
    }
}
