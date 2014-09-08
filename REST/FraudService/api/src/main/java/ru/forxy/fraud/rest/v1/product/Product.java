package ru.forxy.fraud.rest.v1.product;

import ru.forxy.fraud.rest.v1.Entity;
import ru.forxy.fraud.rest.v1.payment.Amount;

public class Product extends Entity {

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
