package ru.forxy.common.pojo.status;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Includes the information about Component health status
 */
public class ComponentStatus implements Serializable {

    public enum ComponentType {
        DB, service, cache
    }

    private String name;

    private String location;

    private StatusType status;

    private Map<String, String> componentConfiguration;

    private ComponentType componentType;

    private long responseTime;

    private Date lastUpdated;

    private String exceptionMessage;

    private String exceptionDetails;

    public ComponentStatus() {
    }

    public ComponentStatus(String name,
                           String location,
                           StatusType status,
                           Map<String, String> componentConfiguration,
                           ComponentType componentType,
                           long responseTime,
                           Date lastUpdated,
                           String exceptionMessage,
                           String exceptionDetails) {
        this.name = name;
        this.location = location;
        this.status = status;
        this.componentConfiguration = componentConfiguration;
        this.componentType = componentType;
        this.responseTime = responseTime;
        this.lastUpdated = lastUpdated;
        this.exceptionMessage = exceptionMessage;
        this.exceptionDetails = exceptionDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Map<String, String> getComponentConfiguration() {
        return componentConfiguration;
    }

    public void setComponentConfiguration(Map<String, String> componentConfiguration) {
        this.componentConfiguration = componentConfiguration;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionDetails() {
        return exceptionDetails;
    }

    public void setExceptionDetails(String exceptionDetails) {
        this.exceptionDetails = exceptionDetails;
    }
}
