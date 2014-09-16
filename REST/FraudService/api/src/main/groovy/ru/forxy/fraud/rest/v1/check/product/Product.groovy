package ru.forxy.fraud.rest.v1.check.product

import ru.forxy.fraud.rest.v1.check.Entity
import ru.forxy.fraud.rest.v1.check.payment.Amount

class Product extends Entity {
    Integer id;
    Amount price;
}
