package com.autoever.apay.models;

public class Store {
    private int id;
    private String businessRegistrationNumber;
    private String storeName;
    private String tokenSystemId;
    private String loginId;


    public Store() {}

    public Store(int id, String businessRegistrationNumber, String storeName, String tokenSystemId, String loginId, String hashedId) {
        this.id = id;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.storeName = storeName;
        this.tokenSystemId = tokenSystemId;
        this.loginId = loginId;
    }

    public int getId() {
        return id;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getTokenSystemId() {
        return tokenSystemId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setTokenSystemId(String tokenSystemId) {
        this.tokenSystemId = tokenSystemId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
