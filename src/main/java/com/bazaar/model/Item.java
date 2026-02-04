package com.bazaar.model;

public class Item {
    private int itemId;
    private String name;
    private String category;
    private String rarity;
    private double npcSellPrice;

    public Item() {
    }

    public Item(int itemId, String name, String category, String rarity, double npcSellPrice) {
        this.itemId = itemId;
        this.name = name;
        this.category = category;
        this.rarity = rarity;
        this.npcSellPrice = npcSellPrice;
    }

    // Getters and Setters
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public double getNpcSellPrice() {
        return npcSellPrice;
    }

    public void setNpcSellPrice(double npcSellPrice) {
        this.npcSellPrice = npcSellPrice;
    }

    @Override
    public String toString() {
        return String.format("Item[id=%d, name='%s', category='%s', rarity='%s', npcPrice=%.2f]",
                itemId, name, category, rarity, npcSellPrice);
    }
}
