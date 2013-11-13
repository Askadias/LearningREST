package ru.forxy.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Bean class to collect all the basic configuration options used by the other system components
 */
public class Configuration {

    private boolean isObjectValidationEnabled;

    private boolean isPayloadLoggingEnabled;

    private boolean isPerformanceLoggingEnabled;

    private boolean isHttpInfoLoggingEnabled;

    private final Map<Object, String> properties;

    public Configuration(){
        properties = new HashMap<Object, String>();
    }

    public Configuration(final Map<Object, String> properties) {
        this.properties =  properties;
    }

    public boolean isObjectValidationEnabled() {
        return isObjectValidationEnabled;
    }

    public void setObjectValidationEnabled(boolean objectValidationEnabled) {
        isObjectValidationEnabled = objectValidationEnabled;
    }

    public boolean isPayloadLoggingEnabled() {
        return isPayloadLoggingEnabled;
    }

    public void setPayloadLoggingEnabled(boolean payloadLoggingEnabled) {
        isPayloadLoggingEnabled = payloadLoggingEnabled;
    }

    public boolean isPerformanceLoggingEnabled() {
        return isPerformanceLoggingEnabled;
    }

    public void setPerformanceLoggingEnabled(boolean performanceLoggingEnabled) {
        isPerformanceLoggingEnabled = performanceLoggingEnabled;
    }

    public boolean isHttpInfoLoggingEnabled() {
        return isHttpInfoLoggingEnabled;
    }

    public void setHttpInfoLoggingEnabled(boolean httpInfoLoggingEnabled) {
        isHttpInfoLoggingEnabled = httpInfoLoggingEnabled;
    }

    public Map<Object, String> getProperties() {
        return properties;
    }

    public String get(Object propertyName) {
        return properties.get(propertyName);
    }
}
