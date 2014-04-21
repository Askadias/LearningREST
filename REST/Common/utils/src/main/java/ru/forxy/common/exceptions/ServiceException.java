package ru.forxy.common.exceptions;

import ru.forxy.common.exceptions.support.StatusTemplate;

/**
 * Base internal service exceptions to mark it as separate error code
 */
public class ServiceException extends RuntimeException {

    private final String statusCode;

    public ServiceException(final StatusTemplate statusTemplate, final Object... args) {
        super(String.format(statusTemplate.getTemplate(), args));
        statusCode = statusTemplate.getStatusCode();
    }

    public ServiceException(final Throwable cause, final String statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    public ServiceException(final String message, final Throwable cause, final String statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public ServiceException(final String message, final String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
