package ru.forxy.fraud.rest.pojo.product;

import ru.forxy.fraud.rest.pojo.Entity;
import ru.forxy.fraud.rest.pojo.payment.Amount;

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
