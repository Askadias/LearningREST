package ru.forxy.fraud.rest.v1.person;

import ru.forxy.fraud.rest.v1.Entity;

import java.util.Date;

public class Statistics extends Entity {
    protected Date lastPurchaseDate;
    protected Date firstPurchaseDate;

    public Date getLastPurchaseDate() {
        return lastPurchaseDate;
    }

    public void setLastPurchaseDate(Date lastPurchaseDate) {
        this.lastPurchaseDate = lastPurchaseDate;
    }

    public Date getFirstPurchaseDate() {
        return firstPurchaseDate;
    }

    public void setFirstPurchaseDate(Date firstPurchaseDate) {
        this.firstPurchaseDate = firstPurchaseDate;
    }
}
