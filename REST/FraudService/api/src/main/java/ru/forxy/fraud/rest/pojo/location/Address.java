package ru.forxy.fraud.rest.pojo.location;

public class Address extends Location {

    protected String addressLine;

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
}