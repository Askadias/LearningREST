package ru.forxy.fraud.rest.pojo.person;

import ru.forxy.fraud.rest.pojo.Entity;

public class Telephone extends Entity {
    protected String areaCode;
    protected String countryAccessCode;
    protected String phoneNumber;
    protected Type type;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCountryAccessCode() {
        return countryAccessCode;
    }

    public void setCountryAccessCode(String countryAccessCode) {
        this.countryAccessCode = countryAccessCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        Home,
        Mobile,
        Business
    }
}