package com.sokeila.personaldata.services;

import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Sex;
import com.sokeila.personaldata.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SsnGenerator {

    private final char[] checkSigns = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y'};

    public String generateSsn(Country country, Sex sex, LocalDate birthDate) {
        String ssn = null;
        if(country == Country.FINLAND) {
            String date = "" + birthDate.getDayOfMonth();
            String month = "" + birthDate.getMonthValue();
            if(month.length() == 1) {
                month = "0" + month;
            }
            String year = "" + birthDate.getYear();
            year = year.substring(2);
            String individualNimber = generateIndividualNumber(sex);

            String number = date + month + year + individualNimber;

            int remainder = Integer.parseInt(number) % 31;
            char checkSign = checkSigns[remainder];

            String decade = "";
            if(birthDate.getYear() >= 1800 && birthDate.getYear() < 1900) {
                decade = "+";
            } else if(birthDate.getYear() >= 1900 && birthDate.getYear() < 2000) {
                decade = "-";
            } else if(birthDate.getYear() >= 2000) {
                decade = "A";
            }

            ssn = date + month + year + decade + individualNimber + checkSign;
        }

        return ssn;
    }

    private String generateIndividualNumber(Sex sex) {
        int individualNumber = RandomUtils.getRandomNumber(2, 899);

        if(sex == Sex.FEMALE & individualNumber % 2 != 0) {
            individualNumber++;
        } else if(sex == Sex.MALE & individualNumber % 2 == 0) {
            individualNumber++;
        }

        if(individualNumber < 10) {
            return "00" + individualNumber;
        } else if(individualNumber < 100) {
            return "0" + individualNumber;
        }

        return "" + individualNumber;
    }
}
