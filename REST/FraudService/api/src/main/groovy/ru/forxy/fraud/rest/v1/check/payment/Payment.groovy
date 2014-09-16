package ru.forxy.fraud.rest.v1.check.payment

import ru.forxy.fraud.rest.v1.check.Entity
import ru.forxy.fraud.rest.v1.check.person.Person

class Payment extends Entity {
    Amount amount;
    String formOfPayment;
    Person payer;
}
