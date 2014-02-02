package ru.forxy.fraud.rest.pojo.location;

import ru.forxy.fraud.rest.pojo.Entity;

public class Location extends Entity {
    protected String city;
    protected String country;

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
}
