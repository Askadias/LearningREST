package ru.forxy.common.exceptions;

import ru.forxy.common.pojo.ErrorEntity;

/**
 * Base Client exception
 */
public class ClientException extends ServiceException{

    private final ErrorEntity errorEntity;

    public ClientException(ErrorEntity errorEntity, String message, EventLogBase eventLogId) {
        super(message, eventLogId);
        this.errorEntity = errorEntity;
    }

    public ClientException(ErrorEntity errorEntity, EventLogBase eventLogId) {
        super(eventLogId);
        this.errorEntity = errorEntity;
    }

    public ClientException(ErrorEntity errorEntity, Throwable cause, String message, EventLogBase eventLogId) {
        super(cause, message, eventLogId);
        this.errorEntity = errorEntity;
    }

    public ClientException(ErrorEntity errorEntity, Throwable cause, EventLogBase eventLogId, Object... args) {
        super(cause, eventLogId, args);
        this.errorEntity = errorEntity;
    }

    public ClientException(ErrorEntity errorEntity, EventLogBase eventLogId, Object... args) {
        super(eventLogId, args);
        this.errorEntity = errorEntity;
    }

    public ClientException(ErrorEntity errorEntity, Throwable cause, EventLogBase eventLogId) {
        super(cause, eventLogId);
        this.errorEntity = errorEntity;
    }

    public ErrorEntity getErrorEntity() {
        return errorEntity;
    }
}
