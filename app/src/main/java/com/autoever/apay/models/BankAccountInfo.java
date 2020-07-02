package com.autoever.apay.models;

import android.view.View;

import com.autoever.apay.R;

public class BankAccountInfo {
    private String bankId;
    private String bankName;
    private String bankAccountNumber;
    private String bankAccountAddDate;
    private Integer bankLogo;

    public BankAccountInfo(String bankId, String bankName, String bankAccountNumber, String bankAccountAddDate) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountAddDate = bankAccountAddDate;

        switch (bankId) {
            case "01":
                this.bankLogo = R.drawable.nonghyub_logo;
                break;
            case "02":
                this.bankLogo = R.drawable.woori_logo;
                break;
            case "03":
                this.bankLogo = R.drawable.shinhan_logo;
                break;
            case "04":
                this.bankLogo = R.drawable.kookmin_logo;
                break;
            case "05":
                this.bankLogo = R.drawable.hana_logo;
                break;
            case "06":
                this.bankLogo = R.drawable.city_logo;
                break;
            case "07":
                this.bankLogo = R.drawable.ibk_logo;
                break;
            case "08":
                this.bankLogo = R.drawable.kbank_logo;
                break;
            case "09":
                this.bankLogo = R.drawable.kakao_bank_logo;
                break;
            default:
                this.bankLogo = null;
        }

    }

    public String getBankId() {
        return bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getBankAccountAddDate() {
        return bankAccountAddDate;
    }

    public Integer getBankLogo() {
        return bankLogo;
    }
}
