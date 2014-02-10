package ru.forxy.fraud.rest.pojo.product;

import ru.forxy.fraud.rest.pojo.Entity;

public class Product extends Entity {

    protected Integer id;

    protected Price price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
