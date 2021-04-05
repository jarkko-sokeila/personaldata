package com.sokeila.personaldata.services.bank;

import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class IbanGenerator {
    private static final Logger log = LoggerFactory.getLogger(IbanGenerator.class);

    private static Properties ibanProperties;
    private static final Map<String, String> letterNumberMap = new HashMap<>();

    static {
        try {
            readPropertiesFile();
            initLetterNumberMap();
        } catch (IOException e) {
            log.error("Error while reading iban properties");
        }
    }

    private static void readPropertiesFile() throws IOException {
        try(InputStream resource = new ClassPathResource("data/bank/iban.properties").getInputStream()) {
            ibanProperties = new Properties();
            ibanProperties.load(resource);
        }
    }

    private static void initLetterNumberMap() {
        letterNumberMap.put("A", "10");
        letterNumberMap.put("B", "11");
        letterNumberMap.put("C", "12");
        letterNumberMap.put("D", "13");
        letterNumberMap.put("E", "14");
        letterNumberMap.put("F", "15");
        letterNumberMap.put("G", "16");
        letterNumberMap.put("H", "17");
        letterNumberMap.put("I", "18");
        letterNumberMap.put("J", "19");
        letterNumberMap.put("K", "20");
        letterNumberMap.put("L", "21");
        letterNumberMap.put("M", "22");
        letterNumberMap.put("N", "23");
        letterNumberMap.put("O", "24");
        letterNumberMap.put("P", "25");
        letterNumberMap.put("Q", "26");
        letterNumberMap.put("R", "27");
        letterNumberMap.put("S", "28");
        letterNumberMap.put("T", "29");
        letterNumberMap.put("U", "30");
        letterNumberMap.put("V", "31");
        letterNumberMap.put("W", "32");
        letterNumberMap.put("X", "33");
        letterNumberMap.put("Y", "34");
        letterNumberMap.put("Z", "35");
    }

    public static void main(String[] args) {
        new IbanGenerator().generateRandomIban(Country.FINLAND);
    }

    public String generateRandomIban(Country country) {
        IBAN iban = country.getIban();
        if(iban == null)
            return null;

        int ibanLength = getIbanLength(iban);
        if(ibanLength == -1)
            return null;

        String ibanNumber = generateIban(iban, ibanLength);

        return ibanNumber;
    }

    private int getIbanLength(IBAN iban) {
        String length = ibanProperties.getProperty(iban.name(), null);
        if(StringUtils.isBlank(length))
            return -1;

        return Integer.parseInt(length);
    }

    private String generateIban(IBAN iban, int ibanLength) {
        StringBuilder builder = new StringBuilder();
        int numberCount = ibanLength - 4;
        for(int i = 0; i < numberCount; i++) {
            if(i == 0) {
                builder.append(RandomUtils.getRandomNumber(1, 9));
            } else {
                builder.append(RandomUtils.getRandomNumber(0, 9));
            }
        }

        String endPart = getEndPart(iban.name());

        String accountNumber = builder.toString();
        String numberStr = accountNumber + endPart;

        //numberStr = "79433846350988151800";
        BigInteger bigInteger = new BigInteger(numberStr);
        BigInteger mod = bigInteger.mod(new BigInteger("97"));
        int checkNumber = (int) (98 - mod.intValue());

        String checkNumberStr;
        if(checkNumber < 10)
            checkNumberStr = "0" + checkNumber;
        else
            checkNumberStr = "" + checkNumber;

        return iban.name() + checkNumberStr + accountNumber;
    }

    private String getEndPart(String name) {
        String first = "" + name.charAt(0);
        String second = "" + name.charAt(1);
        return letterNumberMap.get(first) + letterNumberMap.get(second) + "00";
    }
}
