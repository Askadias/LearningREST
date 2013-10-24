package ru.forxy.common.pojo;

import ru.forxy.common.SystemProperties;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for the all services Responses.
 * Contains all the necessary information about the operation execution status.
 */
public abstract class BaseResponse {

    protected Integer code;

    protected Response.Status status;

    protected List<ResponseMessage> messages = new ArrayList<ResponseMessage>();

    protected String service;

    protected String version;

    public BaseResponse() {
        this.status = Response.Status.OK;
        this.code = status.getStatusCode();
        this.service = SystemProperties.getServiceName();
        this.version = SystemProperties.getServiceVersion();
    }

    public BaseResponse(Throwable exception) {
        this.status = Response.Status.INTERNAL_SERVER_ERROR;
        this.code = status.getStatusCode();
        this.addMessage(new ResponseMessage(exception));
    }

    public BaseResponse(Integer code) {
        this.code = status.getStatusCode();
        this.status = Response.Status.fromStatusCode(code);
    }

    public BaseResponse(Integer code, Response.Status status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public List<ResponseMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ResponseMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ResponseMessage message) {
        messages.add(message);
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format("{code:'%d', status:'%s'}", code, status);
    }
}
