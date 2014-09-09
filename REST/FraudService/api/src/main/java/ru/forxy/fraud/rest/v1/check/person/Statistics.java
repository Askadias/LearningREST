package ru.forxy.fraud.rest.v1.check.person;

import ru.forxy.fraud.rest.v1.check.Entity;

import java.util.Date;

public class Statistics extends Entity {

    private static final long serialVersionUID = -5327241238342172718L;

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
