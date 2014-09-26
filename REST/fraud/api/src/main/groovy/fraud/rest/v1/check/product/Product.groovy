package fraud.rest.v1.check.product

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import fraud.rest.v1.check.Entity
import fraud.rest.v1.check.payment.Amount

@ToString
@EqualsAndHashCode(callSuper = true)
class Product extends Entity {
    Integer id;
    Amount price;
}
