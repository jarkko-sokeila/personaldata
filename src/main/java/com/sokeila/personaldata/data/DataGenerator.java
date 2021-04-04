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

    static {
        try {
            loadFirstNames();
            loadLastNames();
            loadEmailDomains();
            loadCompanyNames();
        } catch (IOException e) {
            log.error("Error while loading data", e);
        }
    }

    public static List<String> getFirstNames(Sex sex, Country country) {
        Objects.requireNonNull(sex, "Sex is mandatory");
        Objects.requireNonNull(country, "Country is mandatory");

        Map<Country, Set<String>> firstNameMap = firstNames.get(sex);
        Set<String> names = getNamesFromMap(country, firstNameMap);

        return new ArrayList<>(names);
    }

    public static List<String> getLastNames(Country country) {
        Objects.requireNonNull(country, "Country is mandatory");

        Set<String> names = getNamesFromMap(country, lastNames);

        return new ArrayList<>(names);
    }

    private static Set<String> getNamesFromMap(Country country, Map<Country, Set<String>> namesMap) {
        Set<String> names = namesMap.get(country);
        if(CollectionUtils.isEmpty(names)) {
            names = namesMap.get(null);
        }
        if(CollectionUtils.isEmpty(names)) {
            throw new IllegalStateException("Could not resolve names for country " + country);
        }
        return names;
    }

    public static List<String> getEmailDomains() {
        return new ArrayList<>(emailDomains);
    }

    public static List<String> getCompanyNames() {
        return new ArrayList<>(companyNames);
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
        loadLastNames(null, "data/lastnames/lastnames.txt");

        for(Country country : Country.values()) {
            try {
                String lastNamesFile = "data/lastnames/lastnames_" + country.getLocale() + ".txt";
                loadLastNames(country, lastNamesFile);
            } catch (IOException e) {
                log.warn("Could not load last names for country " + country);
                log.debug("Error", e);
            }
        }
    }

    private static void loadLastNames(Country country, String source) throws IOException {
        InputStream resource = new ClassPathResource(source).getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)) ) {
            Set<String> lastNamesData = reader.lines().collect(Collectors.toSet());
            lastNames.put(country, lastNamesData);
        }
    }

    private static void loadEmailDomains() {
        emailDomains.addAll(Arrays.asList("gmail.com", "hotmail.com"));
    }

    private static void loadCompanyNames() throws IOException {
        InputStream resource = new ClassPathResource("data/company/company.txt").getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)) ) {
            Set<String> companyNameData = reader.lines().collect(Collectors.toSet());
            companyNames.addAll(companyNameData);
        }
    }
}
