package ru.forxy.fraud.rest.pojo.payment;

import ru.forxy.fraud.rest.pojo.Entity;

public class Payment extends Entity {
    protected Amount amount;
    protected String formOfPayment;

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getFormOfPayment() {
        return formOfPayment;
    }

    public void setFormOfPayment(String formOfPayment) {
        this.formOfPayment = formOfPayment;
    }
}