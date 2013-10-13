package ru.forxy.common.pojo;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Response message to be collected within any service response messages list.
 */
public class ResponseMessage {

    private String message;
    private MessageType type = MessageType.INFO;
    private String stackTrace = null;

    public ResponseMessage() {
    }

    /**
     * Basic INFO message
     *
     * @param message short description about the result of operation.
     */
    public ResponseMessage(String message) {
        this.message = message;
    }

    /**
     * FATAL message type. Description will be retrieved from the exception.
     *
     * @param cause thrown exception
     */
    public ResponseMessage(Throwable cause) {
        this.message = cause.getMessage();
        this.type = MessageType.FATAL;
        this.stackTrace = ExceptionUtils.getStackTrace(cause);
    }

    /**
     * Message will be retrieved from the exception.
     *
     * @param type  on of error types WARN/ERROR/FATAL
     * @param cause thrown exception
     */
    public ResponseMessage(MessageType type, Throwable cause) {
        this.message = cause.getMessage();
        this.type = type;
        this.stackTrace = ExceptionUtils.getStackTrace(cause);
    }

    /**
     * @param message short error description
     * @param type    on of error types WARN/ERROR/FATAL
     * @param cause   thrown exception
     */
    public ResponseMessage(String message, MessageType type, Throwable cause) {
        this.message = message;
        this.type = type;
        this.stackTrace = ExceptionUtils.getStackTrace(cause);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
