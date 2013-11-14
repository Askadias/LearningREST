package ru.forxy.common.logging.support;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.forxy.common.exceptions.CommonExceptions;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.support.Context;

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
                    cause == null ? "N/A" : cause.getMessage());
        }
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }
}