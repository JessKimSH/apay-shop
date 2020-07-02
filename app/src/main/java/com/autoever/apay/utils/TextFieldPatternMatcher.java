package com.autoever.apay.utils;

import java.util.regex.Pattern;

public class TextFieldPatternMatcher implements PatternMatcher {
    private static Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{4}\\d{2}\\d{2}$");

    private static Pattern PHONE_NUMBER_PATTERN = Pattern.compile(
            "^\\d{11}$");

    private static Pattern AUTH_NUMBER_PATTERN = Pattern.compile(
            "^\\d{6}$");

    private static Pattern EMAIL_PATTERN = Pattern.compile(
            "^(.+)@(.+)$");

    private static Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(
            "^[0-9]*$");

    @Override
    public boolean birthdayTypeMatches(String birthDay) {
        return DATE_PATTERN.matcher(birthDay).matches();
    }

    @Override
    public boolean phoneNumberTypeMatches(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    @Override
    public boolean authNumberTypeMatches(String authNumber) {
        return AUTH_NUMBER_PATTERN.matcher(authNumber).matches();
    }

    @Override
    public boolean emailTypeMatches(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    public boolean accountNumberTypeMatches(String accountNumber) {
        return ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }
}
