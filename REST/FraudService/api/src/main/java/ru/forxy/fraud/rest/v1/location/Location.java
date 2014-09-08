package ru.forxy.fraud.rest.v1.location;

import ru.forxy.fraud.rest.v1.Entity;

public class Location extends Entity {
    protected String city;
    protected String country;
    protected Integer postalCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }
}
