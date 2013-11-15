package ru.forxy.common.logging.exceptions;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.forxy.common.exceptions.CommonExceptions;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.logging.support.IExceptionHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Class for handling exceptions connected with database
 */
public class DatabaseExceptionHandler implements IExceptionHandler {

    private String databaseHost = null;

    @Override
    public void handleException(Throwable t) {
        String host = databaseHost != null ? databaseHost : "N/A";

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable cause = ExceptionUtils.getRootCause(t);
        if (cause instanceof ConnectException) {
            throw new ServiceException(CommonExceptions.DatabaseIsNotAvailable.getStatusTemplate(),
                    host, cause.getMessage());
        } else if (cause instanceof SocketTimeoutException) {
            throw new ServiceException(CommonExceptions.DatabaseTimeout.getStatusTemplate(),
                    host, cause.getMessage());
        } else {
            throw new ServiceException(CommonExceptions.UnknownDataBaseException.getStatusTemplate(),
                    cause != null ? cause.getMessage() : t != null ? t.getMessage() : "N/A");
        }
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }
}