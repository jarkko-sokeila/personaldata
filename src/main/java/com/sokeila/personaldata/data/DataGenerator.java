package com.sokeila.personaldata.data;

import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Sex;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class DataGenerator {
    private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);

    private static final Map<Sex, Map<Country, Set<String>>> firstNames = new HashMap<>();
    private static final Map<Country, Set<String>> lastNames = new HashMap<>();
    private static final Set<String> emailDomains = new HashSet<>();
    private static final Set<String> companyNames = new HashSet<>();
    private static final Map<Country, Set<String>> addresses = new HashMap<>();
    private static final Map<Country, Set<String>> streets = new HashMap<>();
    private static final Set<String> carModels = new HashSet<>();

    static {
        try {
            loadFirstNames();
            loadLastNames();
            loadEmailDomains();
            loadCompanyNames();
            loadAddresses();
            loadStreets();
            loadCarModels();
        } catch (IOException e) {
            log.error("Error while loading data", e);
        }
    }

    public static List<String> getFirstNames(Sex sex, Country country) {
        Objects.requireNonNull(sex, "Sex is mandatory");
        Objects.requireNonNull(country, "Country is mandatory");

        Map<Country, Set<String>> firstNameMap = firstNames.get(sex);
        Set<String> names = getValuesFromMap(country, firstNameMap);

        return new ArrayList<>(names);
    }

    public static List<String> getLastNames(Country country) {
        Objects.requireNonNull(country, "Country is mandatory");

        Set<String> names = getValuesFromMap(country, lastNames);

        return new ArrayList<>(names);
    }

    public static List<String> getEmailDomains() {
        return new ArrayList<>(emailDomains);
    }

    public static List<String> getCompanyNames() {
        return new ArrayList<>(companyNames);
    }

    public static List<String> getAddresses(Country country) {
        Objects.requireNonNull(country, "Country is mandatory");

        Set<String> values = getValuesFromMap(country, addresses);
        List<String> result = new ArrayList<>(values);
        Collections.shuffle(result);

        return result;
    }

    public static List<String> getStreets(Country country) {
        Objects.requireNonNull(country, "Country is mandatory");

        Set<String> values = getValuesFromMap(country, streets);

        return new ArrayList<>(values);
    }

    public static List<String> getCarModels() {
        return new ArrayList<>(carModels);
    }

    private static Set<String> getValuesFromMap(Country country, Map<Country, Set<String>> valuesMap) {
        Set<String> values = valuesMap.get(country);
        if(CollectionUtils.isEmpty(values)) {
            values = valuesMap.get(null);
        }
        if(CollectionUtils.isEmpty(values)) {
            throw new IllegalStateException("Could not resolve values for country " + country);
        }
        return values;
    }

    private static void loadFirstNames() throws IOException {
        loadFirstNames(Sex.FEMALE, null,"data/firstnames/females.txt");
        loadFirstNames(Sex.MALE, null,"data/firstnames/males.txt");

        for(Country country : Country.values()) {
            try {
                String femalesNamesFile = "data/firstnames/females_" + country.getLocale() + ".txt";
                loadFirstNames(Sex.FEMALE, country, femalesNamesFile);
            } catch (IOException e) {
                log.warn("Could not load female names for country " + country);
                log.debug("Error", e);
            }

            try {
                String malesNamesFile = "data/firstnames/males_" + country.getLocale() + ".txt";
                loadFirstNames(Sex.MALE, country, malesNamesFile);
            } catch (IOException e) {
                log.warn("Could not load male names for country " + country);
                log.debug("Error", e);
            }
        }
    }

    private static void loadFirstNames(Sex sex, Country country, String source) throws IOException {
        InputStream resource = new ClassPathResource(source).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
            Set<String> names = reader.lines().collect(Collectors.toSet());
            Map<Country, Set<String>> nameMap = firstNames.computeIfAbsent(sex, k -> new HashMap<>());
            nameMap.put(country, names);
        }
    }

    private static void loadLastNames() throws IOException {
        loadCountryData(null, "data/lastnames/lastnames.txt", lastNames);

        for(Country country : Country.values()) {
            try {
                String lastNamesFile = "data/lastnames/lastnames_" + country.getLocale() + ".txt";
                loadCountryData(country, lastNamesFile, lastNames);
            } catch (IOException e) {
                log.warn("Could not load last names for country " + country);
                log.debug("Error", e);
            }
        }
    }

    private static void loadEmailDomains() {
        emailDomains.addAll(Arrays.asList("gmail.com", "hotmail.com", "yahoo.com", "mail.com"));
    }

    private static void loadCompanyNames() throws IOException {
        InputStream resource = new ClassPathResource("data/company/company.txt").getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)) ) {
            Set<String> companyNameData = reader.lines().collect(Collectors.toSet());
            companyNames.addAll(companyNameData);
        }
    }

    private static void loadAddresses() throws IOException {
        loadCountryData(null,"data/address/city.txt", addresses);

        for(Country country : Country.values()) {
            try {
                String citiesFile = "data/address/city_" + country.getLocale() + ".txt";
                loadCountryData(country, citiesFile, addresses);
            } catch (IOException e) {
                log.warn("Could not load cities for country " + country);
                log.debug("Error", e);
            }
        }
    }

    private static void loadStreets() throws IOException {
        loadCountryData(null,"data/address/street.txt", streets);

        for(Country country : Country.values()) {
            try {
                String streetsFile = "data/address/street_" + country.getLocale() + ".txt";
                loadCountryData(country, streetsFile, streets);
            } catch (IOException e) {
                log.warn("Could not load streets for country " + country);
                log.debug("Error", e);
            }
        }
    }

    private static void loadCarModels() throws IOException {
        InputStream resource = new ClassPathResource("data/car/car.txt").getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)) ) {
            Set<String> carModelData = reader.lines().collect(Collectors.toSet());
            carModels.addAll(carModelData);
        }
    }

    private static void loadCountryData(Country country, String source, Map<Country, Set<String>> dataMap) throws IOException {
        InputStream resource = new ClassPathResource(source).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
            Set<String> data = reader.lines().collect(Collectors.toSet());
            dataMap.put(country, data);
        }
    }
}
