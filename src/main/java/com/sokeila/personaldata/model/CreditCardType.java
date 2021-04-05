package com.sokeila.personaldata.model;

import java.util.Arrays;
import java.util.List;

public enum CreditCardType {
    VISA(16, "4"), MasterCard(16, "51", "52", "53", "54", "55"), AmericanExpress(15, "34", "37");

    private final int length;
    private final List<String> cardPrefixNumbers;

    CreditCardType(int length, String... prefixNumbers) {
        this.length = length;
        this.cardPrefixNumbers = Arrays.asList(prefixNumbers);
    }

    public int getLength() {
        return length;
    }

    public List<String> getCardPrefixNumbers() {
        return cardPrefixNumbers;
    }
}
