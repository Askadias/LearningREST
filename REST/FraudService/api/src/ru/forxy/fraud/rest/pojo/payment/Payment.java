package ru.forxy.fraud.rest.pojo.payment;

import ru.forxy.fraud.rest.pojo.Entity;
import ru.forxy.fraud.rest.pojo.person.Person;

public class Payment extends Entity {
    protected Amount amount;
    protected String formOfPayment;
    protected Person owner;

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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}