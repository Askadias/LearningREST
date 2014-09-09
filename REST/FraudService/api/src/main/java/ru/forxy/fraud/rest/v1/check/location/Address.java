package ru.forxy.fraud.rest.v1.check.location;

public class Address extends Location {

    private static final long serialVersionUID = -7497626033927213518L;

    protected String addressLine;

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }
}
