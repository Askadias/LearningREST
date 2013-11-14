package ru.forxy.common.logging.support;

import org.apache.commons.lang.exception.ExceptionUtils;
import ru.forxy.common.exceptions.CommonExceptions;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.support.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Uladzislau_Prykhodzk on 11/14/13.
 */
public class ServiceExceptionHandler implements IExceptionHandler {

    @Override
    public void handleException(Throwable t) {
        //noinspection SuspiciousMethodCalls
        String requestUrl = (String) Context.peek().getFrame().get(Fields.RequestURL);
        requestUrl = requestUrl != null ? requestUrl : "N/A";

        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable cause = ExceptionUtils.getRootCause(t);
        if (cause instanceof ConnectException) {
            throw new ServiceException(CommonExceptions.ServiceIsNotAvailable.getStatusTemplate(),
                    requestUrl, cause.getMessage());
        } else if (cause instanceof SocketTimeoutException) {
            throw new ServiceException(CommonExceptions.ServiceTimeout.getStatusTemplate(),
                    requestUrl, cause.getMessage());
        } else {
            throw new ServiceException(CommonExceptions.UnknownServiceException.getStatusTemplate(),
                    cause == null ? "N/A" : cause.getMessage());
        }
    }
}
