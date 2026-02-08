package com.sokeila.personaldata.services;

import com.sokeila.common.utils.StringUtils;
import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Phone;
import com.sokeila.personaldata.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class PhoneNumberGenerator extends RandomGenerator {
    private static final Logger log = LoggerFactory.getLogger(PhoneNumberGenerator.class);

    private static Properties properties;

    static {
        try {
            readPropertiesFile();
        } catch (IOException e) {
            log.error("Error while reading phone number properties");
        }
    }

    private static void readPropertiesFile() throws IOException {
        try(InputStream resource = new ClassPathResource("data/phonenumber/phonenumber.properties").getInputStream()) {
            properties = new Properties();
            properties.load(resource);
        }
    }

    public Phone generatePhoneNumber(Country country) {
        String countryCode;
        String length;
        Phone phone;
        String phoneNumberPrefixes;

        switch (country) {
            case FINLAND -> {
                countryCode = properties.getProperty("phone-number.country-code." + country.getLanguage(), "+358");
                length = properties.getProperty("phone-number.length." + country.getLanguage(), "10");
                phoneNumberPrefixes = properties.getProperty("phone-number.prefixes." + country.getLanguage(), "");
                phone = generatePhoneNumber(countryCode, length, phoneNumberPrefixes);
                phone.setFullNumber(phone.getCountryCode() + phone.getPhoneNumber().substring(1));
                return phone;
            }
            case UNITED_STATES -> {
                countryCode = properties.getProperty("phone-number.country-code", "+1");
                length = properties.getProperty("phone-number.length", "10");
                phoneNumberPrefixes = properties.getProperty("phone-number.prefixes.", "");
                phone = generatePhoneNumber(countryCode, length, phoneNumberPrefixes);
                phone.setFullNumber(phone.getCountryCode() + phone.getPhoneNumber());
                return phone;
            }
            default -> {
                return null;
            }
        }
    }

    private Phone generatePhoneNumber(String countryCode, String lengthStr, String phoneNumberPrefixes) {
        Phone phone = new Phone();
        phone.setCountryCode(countryCode);
        int length = Integer.parseInt(lengthStr);
        StringBuilder builder = new StringBuilder(length);

        if(StringUtils.isNotBlank(phoneNumberPrefixes)) {
            List<String> items = Arrays.asList(phoneNumberPrefixes.split(","));
            String prefix = getRandomValue(items).trim();
            builder.append(prefix);
            length -= prefix.length();
        }

        for(int i = 0; i < length; i++) {
            builder.append(RandomUtils.getRandomNumber(0,9));
        }
        phone.setPhoneNumber(builder.toString());

        return phone;
    }
}
