package ru.forxy.fraud.rest.pojo.person;

import ru.forxy.fraud.rest.pojo.Entity;

public class Telephone extends Entity {
    protected Integer areaCode;
    protected Integer countryAccessCode;
    protected Integer phoneNumber;
    protected Type type;

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getCountryAccessCode() {
        return countryAccessCode;
    }

    public void setCountryAccessCode(Integer countryAccessCode) {
        this.countryAccessCode = countryAccessCode;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
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