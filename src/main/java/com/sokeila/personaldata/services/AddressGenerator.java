package com.sokeila.personaldata.services;

import com.sokeila.personaldata.data.DataGenerator;
import com.sokeila.personaldata.model.Address;
import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AddressGenerator extends  RandomGenerator {

    public Address generateAddress(Country country) {
        Address address = new Address();

        String randomAddressData = getRandomValue(DataGenerator.getAddresses(country));
        String[] data = randomAddressData.split(";");
        if(data.length != 3) {
            throw new IllegalStateException("Address data is invalid. Data [" + randomAddressData + "]. Expecting csv data with 3 columns");
        }

        String state = data[0];
        String city = data[1];
        String zip = getZip(data[2]);

        address.setState(state);
        address.setCity(city);
        address.setZip(zip);

        address.setStreet(getRandomValue(DataGenerator.getStreets(country)));
        address.setStreetNumber(RandomUtils.getRandomNumber(1, 50));

        return address;
    }

    private String getZip(String zipData) {
        Objects.requireNonNull(zipData, "Zip data can't be null");

        if(!zipData.contains("-"))
            return zipData;

        String[] zipRange = zipData.split("-");
        if(zipRange.length != 2) {
            throw new IllegalStateException("Zip range value is invalid, Data [" + zipData + "]");
        }

        int start = Integer.parseInt(zipRange[0].trim());
        int end = Integer.parseInt(zipRange[1].trim());

        if(start > end) {
            throw new IllegalStateException("Zip range start value must be smaller than end value, Data [" + zipData + "]");
        }

        return "" + RandomUtils.getRandomNumber(start, end);
    }
}
