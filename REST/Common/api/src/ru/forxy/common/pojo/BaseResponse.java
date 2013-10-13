package ru.forxy.common.pojo;

import ru.forxy.common.SystemProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.Integer;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;

/**
 * Base class for the all services Responses.
 * Contains all the necessary information about the operation execution status.
 */
public abstract class BaseResponse {

    protected Integer code;

    protected ResponseStatus status;

    protected List<ResponseMessage> messages = new ArrayList<ResponseMessage>();

    protected String service;

    protected String version;

    public BaseResponse() {
        this.code = 0;
        this.status = ResponseStatus.SUCCESS;
        this.service = SystemProperties.getServiceName();
        this.version = SystemProperties.getServiceVersion();
    }

    public BaseResponse(Throwable exception) {
        this.code = 1;
        this.status = ResponseStatus.FAIL;
        this.addMessage(new ResponseMessage(exception));
    }

    public BaseResponse(Integer code) {
        this.code = code;
        this.status = ResponseStatus.SUCCESS;
    }

    public BaseResponse(Integer code, ResponseStatus status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
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
