package ru.forxy.common.pojo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Exception message entity
 */
@XmlRootElement(name = "error_message")
public class ErrorMessage implements Serializable {
    private String code;
    private List<String> messages;

    public ErrorMessage() {
    }

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.messages = Collections.singletonList(message);
    }

    public ErrorMessage(String code, List<String> messages) {
        this.code = code;
        this.messages =  messages;
    }

    public ErrorMessage(String code, Throwable cause) {
        this.code = code;
        this.messages =  Collections.singletonList(cause.getMessage());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
