package ru.forxy.common.exceptions;

import ru.forxy.common.exceptions.utils.StatusTemplate;

/**
 * Some common service exceptions
 */
public enum BaseServiceExceptions {

    DataBaseIsNotAvailable(new StatusTemplate("1000",
            "Data base on host '%s' is not available. Please check the connection settings.")),
    DataBaseTimeout(new StatusTemplate("1001",
            "The connection timeout exceeded for data base on host '%s'. Please check the connection settings.")),
    NullReference(new StatusTemplate("1002",
            "The %s instance of %s is not set."));

    private StatusTemplate statusTemplate;

    private BaseServiceExceptions(StatusTemplate statusTemplate) {
        this.statusTemplate = statusTemplate;
    }

    public StatusTemplate getStatusTemplate() {
        return statusTemplate;
    }
}
