package ru.forxy.fraud.rest.pojo.person;

import ru.forxy.fraud.rest.pojo.Entity;

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
