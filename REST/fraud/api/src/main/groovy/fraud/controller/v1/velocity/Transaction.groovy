package fraud.controller.v1.velocity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * External POGO for transaction data
 */
@ToString
@EqualsAndHashCode
class Transaction {
    String id
    Date createDate
    Map<String, List<String>> data
}
