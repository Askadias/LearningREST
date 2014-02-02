package ru.forxy.fraud.rest.pojo.person;

public class Traveler extends Person {
    protected Boolean isPrimary;

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}