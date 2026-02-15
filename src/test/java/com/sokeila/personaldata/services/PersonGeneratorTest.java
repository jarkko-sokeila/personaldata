package com.sokeila.personaldata.services;

import com.sokeila.personaldata.model.Country;
import com.sokeila.personaldata.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonGeneratorTest {

    @Autowired
    private PersonGenerator personGenerator;

    @Test
    void testGeneratePersonWithChildren() {
        // Generate multiple persons to ensure we test both cases
        for(int i = 0; i < 10; i++) {
            Person person = personGenerator.generatePerson(Country.FINLAND, true);
            assertNotNull(person);

            if(person.getAge() >= 25) {
                // Persons aged 25+ should have children
                assertNotNull(person.getChildren(), "Person aged " + person.getAge() + " should have children");
                assertTrue(person.getChildren().size() >= 1 && person.getChildren().size() <= 3,
                        "Person should have 1-3 children, but has " + person.getChildren().size());
            } else {
                // Persons under 25 should not have children
                assertNull(person.getChildren(), "Person aged " + person.getAge() + " should not have children");
            }
        }
    }

    @Test
    void testGeneratePersonWithoutChildren() {
        // Test that children generation can be disabled
        Person person = personGenerator.generatePerson(Country.FINLAND, false);
        assertNotNull(person);
        assertNull(person.getChildren(), "Person should not have children when generation is disabled");
    }

    @Test
    void testChildrenHaveSameLastName() {
        // Generate persons until we find one with children
        Person parent = null;
        for(int i = 0; i < 20; i++) {
            Person person = personGenerator.generatePerson(Country.FINLAND, true);
            if(person.getChildren() != null && !person.getChildren().isEmpty()) {
                parent = person;
                break;
            }
        }

        assertNotNull(parent, "Should have generated at least one parent with children");
        assertNotNull(parent.getChildren());

        // All children should have the same last name as parent
        for(Person child : parent.getChildren()) {
            assertEquals(parent.getLastName(), child.getLastName(),
                    "Child should have same last name as parent");
        }
    }

    @Test
    void testChildrenAgeRange() {
        // Generate persons until we find one with children
        Person parent = null;
        for(int i = 0; i < 20; i++) {
            Person person = personGenerator.generatePerson(Country.FINLAND, true);
            if(person.getChildren() != null && !person.getChildren().isEmpty()) {
                parent = person;
                break;
            }
        }

        assertNotNull(parent, "Should have generated at least one parent with children");
        assertNotNull(parent.getChildren());

        // Check that all children are 20-30 years younger than parent
        for(Person child : parent.getChildren()) {
            long yearsDifference = ChronoUnit.YEARS.between(parent.getBirthDate(), child.getBirthDate());
            assertTrue(yearsDifference >= 20 && yearsDifference <= 30,
                    "Child should be 20-30 years younger than parent, but is " + yearsDifference + " years younger");
        }
    }

    @Test
    void testChildrenHaveSameCountry() {
        // Generate persons until we find one with children
        Person parent = null;
        for(int i = 0; i < 20; i++) {
            Person person = personGenerator.generatePerson(Country.UNITED_STATES, true);
            if(person.getChildren() != null && !person.getChildren().isEmpty()) {
                parent = person;
                break;
            }
        }

        assertNotNull(parent, "Should have generated at least one parent with children");
        assertNotNull(parent.getChildren());

        // All children should have the same country as parent
        for(Person child : parent.getChildren()) {
            assertEquals(parent.getCountry(), child.getCountry(),
                    "Child should have same country as parent");
        }
    }

    @Test
    void testChildrenDoNotHaveChildren() {
        // Generate persons until we find one with children
        Person parent = null;
        for(int i = 0; i < 20; i++) {
            Person person = personGenerator.generatePerson(Country.FINLAND, true);
            if(person.getChildren() != null && !person.getChildren().isEmpty()) {
                parent = person;
                break;
            }
        }

        assertNotNull(parent, "Should have generated at least one parent with children");
        assertNotNull(parent.getChildren());

        // Children should not have children (prevent recursion)
        for(Person child : parent.getChildren()) {
            assertNull(child.getChildren(),
                    "Child should not have children to prevent recursion");
        }
    }

    @Test
    void testChildBirthDateNotInFuture() {
        // Generate persons until we find one with children
        Person parent = null;
        for(int i = 0; i < 20; i++) {
            Person person = personGenerator.generatePerson(Country.FINLAND, true);
            if(person.getChildren() != null && !person.getChildren().isEmpty()) {
                parent = person;
                break;
            }
        }

        assertNotNull(parent, "Should have generated at least one parent with children");
        assertNotNull(parent.getChildren());

        LocalDate now = LocalDate.now();
        // No child should have a birth date in the future
        for(Person child : parent.getChildren()) {
            assertFalse(child.getBirthDate().isAfter(now),
                    "Child birth date should not be in the future");
            assertTrue(child.getAge() >= 0,
                    "Child age should not be negative");
        }
    }

    @Test
    void testChildrenHaveAllFields() {
        // Generate persons until we find one with children
        Person parent = null;
        for(int i = 0; i < 20; i++) {
            Person person = personGenerator.generatePerson(Country.FINLAND, true);
            if(person.getChildren() != null && !person.getChildren().isEmpty()) {
                parent = person;
                break;
            }
        }

        assertNotNull(parent, "Should have generated at least one parent with children");
        assertNotNull(parent.getChildren());

        // Verify all children have all fields populated
        for(Person child : parent.getChildren()) {
            assertNotNull(child.getGuid(), "Child should have GUID");
            assertNotNull(child.getCountry(), "Child should have country");
            assertNotNull(child.getGender(), "Child should have gender");
            assertNotNull(child.getBirthDate(), "Child should have birth date");
            assertNotNull(child.getSsn(), "Child should have SSN");
            assertNotNull(child.getAge(), "Child should have age");
            assertNotNull(child.getFirstName(), "Child should have first name");
            assertNotNull(child.getLastName(), "Child should have last name");
            assertNotNull(child.getEmail(), "Child should have email");
            assertNotNull(child.getPhone(), "Child should have phone");
            assertNotNull(child.getMaritalStatus(), "Child should have marital status");
            assertNotNull(child.getPhysical(), "Child should have physical");
            assertNotNull(child.getCompany(), "Child should have company");
            assertNotNull(child.getBankInformation(), "Child should have bank information");
            assertNotNull(child.getAddress(), "Child should have address");
            assertNotNull(child.getGeo(), "Child should have geo");
            assertNotNull(child.getOnline(), "Child should have online");
            assertNotNull(child.getCar(), "Child should have car");
        }
    }
}
