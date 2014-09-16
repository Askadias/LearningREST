package ru.forxy.fraud.rest.v1.check.product

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import ru.forxy.fraud.rest.v1.check.Entity
import ru.forxy.fraud.rest.v1.check.payment.Amount

@ToString
@EqualsAndHashCode(callSuper = true)
class Product extends Entity {
    Integer id;
    Amount price;
}
