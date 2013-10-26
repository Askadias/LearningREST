package ru.forxy.common.exception;

import ru.forxy.common.exception.utils.StatusMessage;

import javax.ws.rs.WebApplicationException;
import java.io.Serializable;

/**
 * Base internal service exception to mark it as separate error code
 */
public class ServiceException extends RuntimeException {

    private String statusCode;

    private StatusMessage statusMessage;

}
