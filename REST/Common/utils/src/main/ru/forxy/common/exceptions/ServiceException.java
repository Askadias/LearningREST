package ru.forxy.common.exceptions;

import ru.forxy.common.exceptions.support.StatusTemplate;

/**
 * Base internal service exceptions to mark it as separate error code
 */
public class ServiceException extends RuntimeException {

    private String statusCode;

    public ServiceException(StatusTemplate statusTemplate, Object... args) {
        super(String.format(statusTemplate.getTemplate(), args));
        statusCode = statusTemplate.getStatusCode();
    }

    public ServiceException(Throwable cause, String statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    public ServiceException(String message, Throwable cause, String statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public ServiceException(String message, String statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
