package ru.forxy.fraud.rest.v1.check.person;

public class Traveler extends Person {

    private static final long serialVersionUID = -4720100217990626007L;

    protected Boolean isPrimary;

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
