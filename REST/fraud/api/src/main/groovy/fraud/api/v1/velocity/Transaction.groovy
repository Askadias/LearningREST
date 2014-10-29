package fraud.api.v1.velocity

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * External POGO for transaction data
 */
@ToString
@EqualsAndHashCode
class Transaction {
    static final String SUBSCRIPTION_PATTERN = 'new_transaction'
    String id
    Date createDate
    Map<String, List<String>> data
}
