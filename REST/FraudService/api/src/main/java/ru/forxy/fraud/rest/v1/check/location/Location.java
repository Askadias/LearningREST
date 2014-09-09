package ru.forxy.fraud.rest.v1.check.location;

import ru.forxy.fraud.rest.v1.check.Entity;

public class Location extends Entity {

    private static final long serialVersionUID = 8039993149241780665L;

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
