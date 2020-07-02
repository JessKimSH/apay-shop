package com.autoever.apay.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Terms {
    private HashMap<String,Boolean> terms;

    public Terms(HashMap<String, Boolean> terms) {
        this.terms = terms;
    }

    public HashMap<String, Boolean> getTerms() {
        return terms;
    }

    public void setTerms(HashMap<String, Boolean> terms) {
        this.terms = terms;
    }

    //
//    public Terms(HashMap<String,Boolean> terms) {
//        this.terms = terms;

//    private String termName;
//    private Boolean isAgreed;
//
//    public Terms(String termName, Boolean isAgreed) {
//        this.termName = termName;
//        this.isAgreed = isAgreed;
//    }
//
//    public String getTermName() {
//        return termName;
//    }
//
//    public void setTermName(String termName) {
//        this.termName = termName;
//    }
//
//    public Boolean getAgreed() {
//        return isAgreed;
//    }
//
//    public void setAgreed(Boolean agreed) {
//        isAgreed = agreed;
//    }
}
