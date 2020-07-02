package com.autoever.apay.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class StorePaymentHistory implements Serializable {

    private String paymentId;
    private String userId;
    private String storeId;
    private String tokenSystemId;
    private int amount;
    private String paymentStatus;
    private String identifier;
    private String createdDate;
    private String userName;

    public StorePaymentHistory() {}

    public StorePaymentHistory(String paymentId, String userId, String storeId, String tokenSystemId, int amount, String paymentStatus, String identifier, String createdDate, String userName) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.storeId = storeId;
        this.tokenSystemId = tokenSystemId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.identifier = identifier;
        this.createdDate = createdDate;
        this.userName = userName;
    }

    public StorePaymentHistory(String paymentId, String userId, String storeId, String tokenSystemId, int amount, String paymentStatus, String identifier, String createdDate) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.storeId = storeId;
        this.tokenSystemId = tokenSystemId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.identifier = identifier;
        this.createdDate = createdDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getUserId() {
        return userId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getTokenSystemId() {
        return tokenSystemId;
    }

    public int getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUserName() {
        return userName;
    }
}
