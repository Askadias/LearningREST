package fraud.controller.v1.check.payment

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import fraud.controller.v1.check.Entity
import fraud.controller.v1.check.person.Person

@ToString
@EqualsAndHashCode(callSuper = true)
class Payment extends Entity {
    Amount amount;
    String formOfPayment;
    Person payer;
}
