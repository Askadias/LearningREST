package ru.forxy.fraud.rest.v1.check.person;

import ru.forxy.fraud.rest.v1.check.Entity;

public class Telephone extends Entity {

    private static final long serialVersionUID = 9145586471000106257L;

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
