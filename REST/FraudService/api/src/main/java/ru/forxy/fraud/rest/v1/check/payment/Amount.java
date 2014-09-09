package ru.forxy.fraud.rest.v1.check.payment;

import ru.forxy.fraud.rest.v1.check.Entity;

import java.math.BigDecimal;

public class Amount extends Entity {

    private static final long serialVersionUID = 1447075735332348774L;

    protected BigDecimal value;
    protected BigDecimal usdValue;
    protected String currency;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getUsdValue() {
        return usdValue;
    }

    public void setUsdValue(BigDecimal usdValue) {
        this.usdValue = usdValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
