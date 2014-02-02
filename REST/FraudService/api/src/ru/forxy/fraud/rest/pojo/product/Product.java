package ru.forxy.fraud.rest.pojo.product;

import ru.forxy.fraud.rest.pojo.Entity;

public class Product extends Entity {
    protected String type;
    protected String name;
    protected Price price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
