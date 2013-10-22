package ru.forxy.common.exceptions;

import ru.forxy.common.pojo.BaseResponse;

import javax.ws.rs.ext.Provider;

/**
 * Exception handler translates internal service exception into custom service response
 */
@Provider
public interface ExceptionHandler<E extends ServiceException, T extends BaseResponse> {
    T toResponse(E exception);
}
