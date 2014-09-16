package ru.forxy.fraud.rest.v1.check.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import ru.forxy.fraud.rest.v1.check.Entity

@ToString
@EqualsAndHashCode(callSuper = true)
class Statistics extends Entity {
    Date lastPurchaseDate;
    Date firstPurchaseDate;
}
