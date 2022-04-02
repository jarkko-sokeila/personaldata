package com.sokeila.personaldata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

@JsonPropertyOrder({ "guid", "country", "gender", "birthDateString", "ssn" })
public class Person {
    private String guid;
    private Country country;
    private Gender gender;
    @JsonIgnore
    private LocalDate birthDate;
    @JsonProperty(value = "birthDate")
    private String birthDateString;
    private String ssn;
    private Integer age;
    private String firstName;
    private String middleName;
    private String lastName;
    private String initials;
    private String email;
    private Phone phone;
    private String maritalStatus;

    private Address address;
    private Physical physical;
    private Company company;
    private BankInformation bankInformation;
    private Geo geo;
    private Online online;
    private Car car;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthDateString() {
        return birthDateString;
    }

    public void setBirthDateString(String birthDateString) {
        this.birthDateString = birthDateString;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        generateInitials();
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
        generateInitials();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        generateInitials();
    }

    public String getInitials() {
        return initials;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Physical getPhysical() {
        return physical;
    }

    public void setPhysical(Physical physical) {
        this.physical = physical;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public BankInformation getBankInformation() {
        return bankInformation;
    }

    public void setBankInformation(BankInformation bankInformation) {
        this.bankInformation = bankInformation;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public Online getOnline() {
        return online;
    }

    public void setOnline(Online online) {
        this.online = online;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    private void generateInitials() {
        this.initials = "";
        if(StringUtils.isNotBlank(firstName)) {
            this.initials += firstName.substring(0, 1). toUpperCase() + ".";
        }
        if(StringUtils.isNotBlank(middleName)) {
            this.initials += " " + middleName.substring(0, 1). toUpperCase() + ".";
        }
        if(StringUtils.isNotBlank(lastName)) {
            this.initials += " " + lastName.substring(0, 1). toUpperCase() + ".";
        }
    }
}
