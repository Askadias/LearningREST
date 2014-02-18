package ru.forxy.fraud.exceptions;

import ru.forxy.common.exceptions.support.StatusTemplate;

/**
 * Fraud service specific exceptions
 */
public enum FraudServiceExceptions {

    IDSouldNotBeNull(new StatusTemplate("3000", "Transaction id sould not be null.")),
    TransactionNotFound(new StatusTemplate("3001", "Transaction with id %l is not found.")),
	CannotRetrieveCassandraSession(new StatusTemplate("3002", "Cannot retrieve cassandra connection."));

    private final StatusTemplate statusTemplate;

    private FraudServiceExceptions(final StatusTemplate statusTemplate) {
        this.statusTemplate = statusTemplate;
    }

    public StatusTemplate getStatusTemplate() {
        return statusTemplate;
    }
}
