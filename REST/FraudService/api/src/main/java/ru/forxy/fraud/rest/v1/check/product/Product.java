package ru.forxy.fraud.rest.v1.check.product;

import ru.forxy.fraud.rest.v1.check.Entity;
import ru.forxy.fraud.rest.v1.check.payment.Amount;

public class Product extends Entity {

    private static final long serialVersionUID = 2378160794520350582L;

    protected Integer id;
    protected Amount price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Amount getPrice() {
        return price;
    }

    public void setPrice(Amount price) {
        this.price = price;
    }
}
