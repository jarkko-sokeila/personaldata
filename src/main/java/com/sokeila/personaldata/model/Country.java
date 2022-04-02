package com.sokeila.personaldata.model;

import com.sokeila.personaldata.services.bank.IBAN;

import java.util.Locale;

public enum Country {
    FINLAND(new Locale("fi"), IBAN.FI), UNITED_STATES(Locale.US);

    private final Locale locale;
    private IBAN iban;

    Country(Locale locale, IBAN iban) {
        this.locale = locale;
        this.iban = iban;
    }

    Country(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public IBAN getIban() {
        return iban;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public static Country fromString(String text) {
        for (Country c : Country.values()) {
            if (c.locale.getCountry().equalsIgnoreCase(text) || c.toString().equalsIgnoreCase(text)) {
                return c;
            }
        }
        return null;
    }
}
