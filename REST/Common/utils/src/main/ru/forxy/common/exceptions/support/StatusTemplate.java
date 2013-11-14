package ru.forxy.common.exceptions.support;

/**
 * Basic class for message templates
 */
public class StatusTemplate {

    private String statusCode;

    private String template;

    public StatusTemplate(String statusCode, String template) {
        this.statusCode = statusCode;
        this.template = template;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getTemplate() {
        return template;
    }
}
