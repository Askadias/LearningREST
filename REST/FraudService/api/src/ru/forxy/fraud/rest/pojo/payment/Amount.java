package ru.forxy.fraud.rest.pojo.payment;

import ru.forxy.fraud.rest.pojo.Entity;

import java.math.BigDecimal;

public class Amount extends Entity {
    protected BigDecimal value;
    protected String currency;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}