package com.autoever.apay.models;

public class PaymentHistory {
    private String shopName;
    private String purchaseAmount;
    private String purchaseDate;
    private int type;

    public PaymentHistory(String shopName, String purchaseAmount, String purchaseDate, int type) {
        this.shopName = shopName;
        this.purchaseAmount = purchaseAmount;
        this.purchaseDate = purchaseDate;
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public int getType() {
        return type;
    }
}
