package com.autoever.apay.utils;

public interface PatternMatcher {
    boolean birthdayTypeMatches(String birthDay);
    boolean phoneNumberTypeMatches(String phoneNumber);
    boolean authNumberTypeMatches(String authNumber);
    boolean emailTypeMatches(String email);
    boolean accountNumberTypeMatches(String accountNumber);
}
