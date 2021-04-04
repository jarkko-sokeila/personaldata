package com.sokeila.personaldata.model;

public enum Country {
    FINLAND("fi"), USA("us");

    private String locale;

    Country(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }
}
