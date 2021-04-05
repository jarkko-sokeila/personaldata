package com.sokeila.personaldata.services;

import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Phone;
import com.sokeila.personaldata.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class PhoneNumberGenerator {
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

        switch (country) {
            case FINLAND:
                countryCode = properties.getProperty("phonenumber.country-code." + country.getLocale(), "+358");
                length = properties.getProperty("phonenumber.length." + country.getLocale(), "10");
                phone = generatePhoneNumber(countryCode, length);
                phone.setFullNumber(phone.getCountryCode() + phone.getPhoneNumber().substring(1));
                return phone;
            case UNITED_STATES:
                countryCode = properties.getProperty("phonenumber.country-code", "+1");
                length = properties.getProperty("phonenumber.length", "10");
                phone = generatePhoneNumber(countryCode, length);
                phone.setFullNumber(phone.getCountryCode() + phone.getPhoneNumber());
                return phone;
            default:
                return null;
        }
    }

    private Phone generatePhoneNumber(String countryCode, String lengthStr) {
        Phone phone = new Phone();
        phone.setCountryCode(countryCode);
        int length = Integer.parseInt(lengthStr);
        StringBuilder builder = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            builder.append(RandomUtils.getRandomNumber(0,9));
        }
        phone.setPhoneNumber(builder.toString());

        return phone;
    }
}
