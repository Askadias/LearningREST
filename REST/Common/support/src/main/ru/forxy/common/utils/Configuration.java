package ru.forxy.common.utils;

/**
 * Bean class to collect all the basic configuration options used by the other system components
 */
public class Configuration {

    private boolean isObjectValidationEnabled;

    private boolean isPayloadLoggingEnabled;

    private boolean isPerformanceLoggingEnabled;

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
}
