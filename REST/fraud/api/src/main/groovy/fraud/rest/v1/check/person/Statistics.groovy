package fraud.rest.v1.check.person

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import fraud.rest.v1.check.Entity

@ToString
@EqualsAndHashCode(callSuper = true)
class Statistics extends Entity {
    Date lastPurchaseDate;
    Date firstPurchaseDate;
}
