package fraud.controller.v1.check.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import fraud.controller.v1.check.Entity

@ToString
@EqualsAndHashCode(callSuper = true)
class Statistics extends Entity {
    Date lastPurchaseDate;
    Date firstPurchaseDate;
}
