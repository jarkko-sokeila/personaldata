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
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.ArrayList;

@Service
public class PersonGenerator extends RandomGenerator {
    private static final Logger log = LoggerFactory.getLogger(PersonGenerator.class);

    private final PhoneNumberGenerator phoneNumberGenerator;
    private final IbanGenerator ibanGenerator;
    private final CreditCardGenerator creditCardGenerator;
    private final AddressGenerator addressGenerator;
    private final SsnGenerator ssnGenerator;
    private final OnlineGenerator onlineGenerator;

    @Autowired
    public PersonGenerator(PhoneNumberGenerator phoneNumberGenerator, IbanGenerator ibanGenerator, CreditCardGenerator creditCardGenerator, AddressGenerator addressGenerator, SsnGenerator ssnGenerator, OnlineGenerator onlineGenerator) {
        this.phoneNumberGenerator = phoneNumberGenerator;
        this.ibanGenerator = ibanGenerator;
        this.creditCardGenerator = creditCardGenerator;
        this.addressGenerator = addressGenerator;
        this.ssnGenerator = ssnGenerator;
        this.onlineGenerator = onlineGenerator;
    }

    public Person generatePerson() {
        Country country = getRandomNationality();
        return generatePerson(country);
    }

    public Person generatePerson(Country country) {
        return generatePerson(country, true);
    }

    public Person generatePerson(Country country, boolean generateChildren) {
        if(country == null) {
            country = getRandomNationality();
        }
        Gender gender = getRandomGender();

        Person person = new Person();
        person.setGuid(UUID.randomUUID().toString());
        person.setCountry(country);
        person.setGender(gender);
        person.setBirthDate(getRandomBirthDate());
        person.setBirthDateString(formatBirthDate(person.getBirthDate(), country));
        person.setSsn(ssnGenerator.generateSsn(country, gender, person.getBirthDate()));
        person.setAge(calculateAge(person.getBirthDate()));
        person.setFirstName(getRandomFirstName(country, gender));
        person.setMiddleName(getRandomFirstName(country, gender));
        person.setLastName(getRandomLastname(country));
        setEmail(person);
        person.setPhone(phoneNumberGenerator.generatePhoneNumber(country));
        person.setMaritalStatus(getMaritalStatus(person.getAge()));
        person.setPhysical(generatePhysical());
        person.setCompany(getRandomCompany());
        person.setBankInformation(getBankInformation(country));
        person.setAddress(addressGenerator.generateAddress(country));
        person.setGeo(getRandomGeo());
        person.setOnline(onlineGenerator.generateOnline(person.getFirstName(), person.getLastName()));
        person.setCar(getRandomCar());

        // Generate children if requested and parent is old enough
        if(generateChildren && person.getAge() >= 25) {
            person.setChildren(generateChildrenList(person));
        }

        return person;
    }

    private String formatBirthDate(LocalDate birthDate, Country country) {
        return birthDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(country.getLocale()));
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

    private String getRandomFirstName(Country country, Gender gender) {
        log.info("Get random first name. Country {}, Gender {}", country, gender);
        return getRandomValue(DataGenerator.getFirstNames(gender, country));
    }

    private String getRandomLastname(Country country) {
        log.info("Get random last name. Country {}", country);
        return getRandomValue(DataGenerator.getLastNames(country));
    }

    private void setEmail(Person person) {
        String domain = getRandomValue(DataGenerator.getEmailDomains());

        String email = person.getFirstName().toLowerCase() + person.getLastName().toLowerCase() + "@" + domain;
        person.setEmail(email);
        if(DataGenerator.isFakeMailDomain(domain)) {
            String emailLink = "https://www.fakemailgenerator.com/#/" + domain + "/" + person.getFirstName().toLowerCase() + person.getLastName().toLowerCase() + "/";
            person.setEmailLink(emailLink);
        }
    }

    private String getMaritalStatus(Integer age) {
        if(age < 18) {
            return "Single";
        } else if(age > 18 && age < 30) {
            return getRandomValue(Arrays.asList("Married", "Single"));
        } else if(age > 18 && age < 55) {
            return getRandomValue(Arrays.asList("Married", "Single", "Divorced"));
        }

        return getRandomValue(Arrays.asList("Married", "Single", "Divorced", "Widowed"));
    }

    private Country getRandomNationality() {
        List<Country> list = Arrays.asList(Country.values());

        return getRandomValue(list);
    }

    private Gender getRandomGender() {
        List<Gender> list = Arrays.asList(Gender.values());

        return getRandomValue(list);
    }

    private Physical generatePhysical() {
        Physical physical = new Physical();

        physical.setBloodType(generateRandomBloodType());
        physical.setHairColor(getRandomValue(Arrays.asList("Black", "White", "Gray", "Red", "Light brown", "Brown", "Dark brown", "Blond")));
        physical.setEyesColor(getRandomValue(Arrays.asList("Brown", "Green", "Blue", "Hazel")));

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

    private Car getRandomCar() {
        Car car = new Car();
        car.setModel(getRandomValue(DataGenerator.getCarModels()));
        car.setYear(RandomUtils.getRandomNumber(1970, 2022));

        return car;
    }

    private List<Person> generateChildrenList(Person parent) {
        int numberOfChildren = RandomUtils.getRandomNumber(1, 3);
        List<Person> children = new ArrayList<>();

        for(int i = 0; i < numberOfChildren; i++) {
            children.add(generateChild(parent));
        }

        return children;
    }

    private Person generateChild(Person parent) {
        // Calculate child's birth date (20-30 years younger)
        int yearsYounger = RandomUtils.getRandomNumber(20, 30);
        LocalDate childBirthDate = parent.getBirthDate().plusYears(yearsYounger);

        // Ensure child's birth date is not in the future
        LocalDate now = LocalDate.now();
        if(childBirthDate.isAfter(now)) {
            childBirthDate = now.minusYears(1);
        }

        Gender childGender = getRandomGender();
        Country country = parent.getCountry();

        // Create child with same generation logic as parent
        Person child = new Person();
        child.setGuid(UUID.randomUUID().toString());
        child.setCountry(country);
        child.setGender(childGender);
        child.setBirthDate(childBirthDate);
        child.setBirthDateString(formatBirthDate(childBirthDate, country));
        child.setSsn(ssnGenerator.generateSsn(country, childGender, childBirthDate));
        child.setAge(calculateAge(childBirthDate));
        child.setFirstName(getRandomFirstName(country, childGender));
        child.setMiddleName(getRandomFirstName(country, childGender));
        child.setLastName(parent.getLastName()); // CRITICAL: Same last name as parent
        setEmail(child);
        child.setPhone(phoneNumberGenerator.generatePhoneNumber(country));
        child.setMaritalStatus(getMaritalStatus(child.getAge()));
        child.setPhysical(generatePhysical());
        child.setCompany(getRandomCompany());
        child.setBankInformation(getBankInformation(country));
        child.setAddress(addressGenerator.generateAddress(country));
        child.setGeo(getRandomGeo());
        child.setOnline(onlineGenerator.generateOnline(child.getFirstName(), child.getLastName()));
        child.setCar(getRandomCar());

        // CRITICAL: Prevent recursion - children do not have children
        child.setChildren(null);

        return child;
    }
}
