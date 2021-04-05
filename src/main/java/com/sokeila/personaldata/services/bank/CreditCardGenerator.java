package com.sokeila.personaldata.services.bank;

import com.sokeila.personaldata.model.CreditCard;
import com.sokeila.personaldata.model.CreditCardType;
import com.sokeila.personaldata.services.RandomGenerator;
import com.sokeila.personaldata.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CreditCardGenerator extends RandomGenerator {
    private static final Logger log = LoggerFactory.getLogger(CreditCardGenerator.class);

    public static void main(String[] args) {
        new CreditCardGenerator().generateCreditCard(CreditCardType.MasterCard);
    }

    public CreditCard generateCreditCard(CreditCardType creditCardType) {
        Objects.requireNonNull(creditCardType, "creditCardType can't be null");
        log.info("Generate new {}", creditCardType);
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardType(creditCardType);

        int validToMonth = RandomUtils.getRandomNumber(1, 12);
        String validToYear = "" + (LocalDateTime.now().getYear() + RandomUtils.getRandomNumber(1, 3));
        creditCard.setValid((validToMonth < 10 ? "0" + validToMonth : validToMonth) + "/" + validToYear.substring(2));
        creditCard.setCvv2("" + RandomUtils.getRandomNumber(100, 999));
        creditCard.setCardNumber(generateRandomCreditCardNumber(creditCardType));

        return creditCard;
    }

    private String generateRandomCreditCardNumber(CreditCardType creditCardType) {
        String prefix = getRandomValue(creditCardType.getCardPrefixNumbers());

        // Numbers to generate depends length of prefix. Also last check number is deducted
        int numbersToGenerate = creditCardType.getLength() - prefix.length() - 1;
        StringBuilder cardNumberBuilder = new StringBuilder();
        cardNumberBuilder.append(prefix);
        for(int i = 0; i < numbersToGenerate; i++) {
            cardNumberBuilder.append(RandomUtils.getRandomNumber(0, 9));
        }

        String cardNumber = cardNumberBuilder.toString();
        int checkDigit = calculateCheckDigit(cardNumberBuilder.reverse());

        return cardNumber + checkDigit;
    }

    private int calculateCheckDigit(StringBuilder reversedCardNumber) {
        int sum = 0;
        for(int i = 0; i < reversedCardNumber.length(); i++) {
            String numberStr = "" + reversedCardNumber.charAt(i);
            int number = Integer.parseInt(numberStr);
            if(i % 2 == 0) {
                int doubleValue = number * 2;
                String doubleValueStr = "" + doubleValue;
                if(doubleValue >= 10) {
                    number = Integer.parseInt("" + doubleValueStr.charAt(0)) + Integer.parseInt("" + doubleValueStr.charAt(1));
                } else {
                    number = doubleValue;
                }
            }

            sum += number;
        }

        return (sum * 9) % 10;
    }
}
