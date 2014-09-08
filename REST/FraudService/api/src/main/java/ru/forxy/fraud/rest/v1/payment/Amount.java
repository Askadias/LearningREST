package ru.forxy.fraud.rest.v1.payment;

import ru.forxy.fraud.rest.v1.Entity;

public class Amount extends Entity {
    protected double value;
    protected double usdValue;
    protected String currency;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getUsdValue() {
        return usdValue;
    }

    public void setUsdValue(double usdValue) {
        this.usdValue = usdValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
