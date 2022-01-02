package com.sokeila.personaldata.services;

import com.sokeila.personaldata.data.DataGenerator;
import com.sokeila.personaldata.model.*;
import com.sokeila.personaldata.services.bank.CreditCardGenerator;
import com.sokeila.personaldata.services.bank.IbanGenerator;
import com.sokeila.personaldata.utils.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PersonGenerator extends RandomGenerator {
    private static final Logger log = LoggerFactory.getLogger(PersonGenerator.class);

    private final PhoneNumberGenerator phoneNumberGenerator;
    private final IbanGenerator ibanGenerator;
    private final CreditCardGenerator creditCardGenerator;
    private final AddressGenerator addressGenerator;
    private final SsnGenerator ssnGenerator;

    @Autowired
    public PersonGenerator(PhoneNumberGenerator phoneNumberGenerator, IbanGenerator ibanGenerator, CreditCardGenerator creditCardGenerator, AddressGenerator addressGenerator, SsnGenerator ssnGenerator) {
        this.phoneNumberGenerator = phoneNumberGenerator;
        this.ibanGenerator = ibanGenerator;
        this.creditCardGenerator = creditCardGenerator;
        this.addressGenerator = addressGenerator;
        this.ssnGenerator = ssnGenerator;
    }

    public Person generatePerson() {
        Country country = getRandomNationality();
        return generatePerson(country);
    }

    public Person generatePerson(Country country) {
        if(country == null) {
            country = getRandomNationality();
        }
        Sex sex = getRandomSex();

        Person person = new Person();
        person.setGuid(UUID.randomUUID().toString());
        person.setCountry(country);
        person.setSex(sex);
        person.setBirthDate(getRandomBirthDate());
        person.setSsn(ssnGenerator.generateSsn(country, sex, person.getBirthDate()));
        person.setAge(calculateAge(person.getBirthDate()));
        person.setFirstName(getRandomFirstName(country, sex));
        person.setLastName(getRandomLastname(country));
        person.setEmail(getRandomEmail(person.getFirstName(), person.getLastName()));
        person.setPhone(phoneNumberGenerator.generatePhoneNumber(country));
        person.setPhysical(generatePhysical());
        person.setCompany(getRandomCompany());
        person.setBankInformation(getBankInformation(country));
        person.setAddress(addressGenerator.generateAddress(country));
        person.setGeo(getRandomGeo());

        return person;
    }

    private LocalDate getRandomBirthDate() {
        int years = RandomUtils.getRandomNumber(1,99);
        int month = RandomUtils.getRandomNumber(1,12);
        int dayOfMonth;
        if(month == 2)
            dayOfMonth = RandomUtils.getRandomNumber(1,28);
        else if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
            dayOfMonth = RandomUtils.getRandomNumber(1,31);
        else
            dayOfMonth = RandomUtils.getRandomNumber(1,30);

        LocalDate birthDay = LocalDate.now();
        log.info("Random birthdate day of month: {}, month: {}, years: {}", dayOfMonth, month, years);

        birthDay = birthDay.minusYears(years);
        birthDay = birthDay.withMonth(month);
        birthDay = birthDay.withDayOfMonth(dayOfMonth);

        return birthDay;
    }

    private Integer calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        long years = ChronoUnit.YEARS.between(birthDate, now);

        return (int) years;
    }

    private String getRandomFirstName(Country country, Sex sex) {
        log.info("Get random first name. Country {}, sex {}", country, sex);
        return getRandomValue(DataGenerator.getFirstNames(sex, country));
    }

    private String getRandomLastname(Country country) {
        log.info("Get random last name. Country {}", country);
        return getRandomValue(DataGenerator.getLastNames(country));
    }

    private String getRandomEmail(String firstName, String lastName) {
        String domain = getRandomValue(DataGenerator.getEmailDomains());

        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + domain;
    }

    private Country getRandomNationality() {
        List<Country> list = Arrays.asList(Country.values());

        return getRandomValue(list);
    }

    private Sex getRandomSex() {
        List<Sex> list = Arrays.asList(Sex.values());

        return getRandomValue(list);
    }

    private Physical generatePhysical() {
        Physical physical = new Physical();

        physical.setBloodType(generateRandomBloodType());
        physical.setHairColor(getRandomValue(Arrays.asList("Black", "White", "Gray", "Red", "Light brown", "Brown", "Dark brown", "Blond")));

        return physical;
    }

    private String generateRandomBloodType() {
        List<String> bloodTypes = Arrays.asList("AB+","AB−","A+","A−","B+","B−","O+","O−");
        return getRandomValue(bloodTypes);
    }

    private Company getRandomCompany() {
        Company company = new Company();
        company.setName(getRandomValue(DataGenerator.getCompanyNames()));
        company.setSalary("" + RandomUtils.getRandomNumber(15, 60) * 100);

        return company;
    }

    private BankInformation getBankInformation(Country country) {
        String iban = ibanGenerator.generateRandomIban(country);
        BankInformation bankInformation = new BankInformation();
        bankInformation.setIban(iban);
        bankInformation.setCreditCard(generateRandomCreditCard());

        return bankInformation;
    }

    private CreditCard generateRandomCreditCard() {
        List<CreditCardType> list = Arrays.asList(CreditCardType.values());
        CreditCardType creditCardType = getRandomValue(list);

        return creditCardGenerator.generateCreditCard(creditCardType);
    }

    private Geo getRandomGeo() {
        Geo geo = new Geo();

        double latitude = -90 + 180 * getRandomGenerator().nextDouble();
        double longitude = -180 + 360 * getRandomGenerator().nextDouble();

        double scale = Math.pow(10, 4);
        latitude = Math.round(latitude * scale) / scale;
        longitude = Math.round(longitude * scale) / scale;
        geo.setLatitude(latitude);
        geo.setLongitude(longitude);
        geo.setGeoCoordinates(latitude + ", " + longitude);

        return geo;
    }
}
