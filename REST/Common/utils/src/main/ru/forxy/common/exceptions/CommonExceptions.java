package ru.forxy.common.exceptions;

import ru.forxy.common.exceptions.support.StatusTemplate;

/**
 * Some common service exceptions
 */
public enum CommonExceptions {

    ServiceIsNotAvailable(
            new StatusTemplate("1000", "Service at '%s' is not available. Please check the connection settings: %s")),
    ServiceTimeout(new StatusTemplate("1001",
            "The connection timeout exceeded for service at '%s'. Please check the connection settings.")),
    UnknownServiceException(new StatusTemplate("1002", "Internal error during database processing: %s")),
    DatabaseIsNotAvailable(
            new StatusTemplate("1010", "Database at '%s' is not available. Please check the connection settings: %s")),
    DatabaseTimeout(new StatusTemplate("1011",
            "The connection timeout exceeded for database at '%s'. Please check the connection settings.")),
    UnknownDataBaseException(new StatusTemplate("1012", "Unknown internal service error: %s")),
    NullReference(new StatusTemplate("1003", "The %s instance of %s is not set."));

    private final StatusTemplate statusTemplate;

    private CommonExceptions(final StatusTemplate statusTemplate) {
        this.statusTemplate = statusTemplate;
    }

    public StatusTemplate getStatusTemplate() {
        return statusTemplate;
    }
}
