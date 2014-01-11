package ru.forxy.common.logging.exceptions;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.forxy.common.exceptions.CommonExceptions;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.logging.support.IExceptionHandler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Class for handling exceptions connected with remote service call or basic service implementation
 */
public class ServiceExceptionHandler implements IExceptionHandler {

    private String remoteServiceHost = null;

    @Override
    public void handleException(Throwable t) {
        String host = remoteServiceHost != null ? remoteServiceHost : "N/A";

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable cause = ExceptionUtils.getRootCause(t);
        if (cause instanceof ConnectException) {
            throw new ServiceException(CommonExceptions.ServiceIsNotAvailable.getStatusTemplate(), host,
                    cause.getMessage());
        } else if (cause instanceof SocketTimeoutException) {
            throw new ServiceException(CommonExceptions.ServiceTimeout.getStatusTemplate(), host, cause.getMessage());
        } else {
            throw new ServiceException(CommonExceptions.UnknownServiceException.getStatusTemplate(),
                    cause != null ? cause.getMessage() : t != null ? t.getMessage() : "N/A");
        }
    }

    public void setRemoteServiceHost(String remoteServiceHost) {
        this.remoteServiceHost = remoteServiceHost;
    }
}
