package com.sokeila.personaldata.model;

import com.sokeila.personaldata.services.bank.IBAN;

public enum Country {
    FINLAND("fi", IBAN.FI), UNITED_STATES("us");

    private final String locale;
    private IBAN iban;

    Country(String locale, IBAN iban) {
        this.locale = locale;
        this.iban = iban;
    }

    Country(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public IBAN getIban() {
        return iban;
    }
}
