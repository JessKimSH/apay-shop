package com.autoever.apay.models;

public class VirtualAccount {
    private int id;
    private String createdDate;
    private int value;
    private int subscriberId;
    private int tokenSystemId;
    private String accountNumber;
    private String accountName;

    public VirtualAccount() {}

    public VirtualAccount(int id, String createdDate, int value, int subscriberId, int tokenSystemId, String accountNumber, String accountName) {
        this.id = id;
        this.createdDate = createdDate;
        this.value = value;
        this.subscriberId = subscriberId;
        this.tokenSystemId = tokenSystemId;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

    public int getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getValue() {
        return value;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public int getTokenSystemId() {
        return tokenSystemId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
