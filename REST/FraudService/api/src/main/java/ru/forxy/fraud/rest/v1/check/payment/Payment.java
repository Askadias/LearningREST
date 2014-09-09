package ru.forxy.fraud.rest.v1.check.payment;

import ru.forxy.fraud.rest.v1.check.Entity;
import ru.forxy.fraud.rest.v1.check.person.Person;

public class Payment extends Entity {

    private static final long serialVersionUID = 5493500173255604561L;

    protected Amount amount;
    protected String formOfPayment;
    protected Person payer;

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

    public Person getPayer() {
        return payer;
    }

    public void setPayer(Person payer) {
        this.payer = payer;
    }
}
