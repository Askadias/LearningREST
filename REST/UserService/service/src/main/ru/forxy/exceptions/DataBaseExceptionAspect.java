package ru.forxy.exceptions;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.forxy.common.exceptions.ServiceException;

import java.net.ConnectException;

/**
 * Data access exception processor
 */
public class DataBaseExceptionAspect {
    public void processException(Exception e) {
        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable t = ExceptionUtils.getRootCause(e);
        if (t instanceof ConnectException) {
            throw new ServiceException(UserServiceExceptions.CouldNotConnectToDataBase.getStatusTemplate(), t.getMessage());
        } else {
            throw new ServiceException(UserServiceExceptions.UnknownDataBaseException.getStatusTemplate(), t.getMessage());
        }
    }
}
