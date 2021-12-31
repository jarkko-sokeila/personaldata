package com.sokeila.personaldata.api;

import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Person;
import com.sokeila.personaldata.services.PersonGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class PersonalDataApi {

    private final PersonGenerator personGenerator;

    @Autowired
    public PersonalDataApi(PersonGenerator personGenerator) {
        this.personGenerator = personGenerator;
    }

    @RequestMapping(value = "person", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getPerson(@RequestParam(required = false, name = "country") String country) {
        Country countryValue = Country.fromString(country);

        return personGenerator.generatePerson(countryValue);
    }
}
