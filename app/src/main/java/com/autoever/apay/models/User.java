package com.autoever.apay.models;

public class User {
    private String profileImage;
    private String userName;
    private String lastConnectedDate;
    private Long balanceLeft;

    public User(String profileImage, String userName, String lastConnectedDate, Long balanceLeft) {
        this.profileImage = profileImage;
        this.userName = userName;
        this.lastConnectedDate = lastConnectedDate;
        this.balanceLeft = balanceLeft;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastConnectedDate() {
        return lastConnectedDate;
    }

    public Long getBalanceLeft() {
        return balanceLeft;
    }
}
