package ru.forxy.common.pojo;

/**
 * Service response contains the list of messages with the fallowing statuses:
 * {@code INFO} - basic message about the result of operation
 * {@code WARN} - if something unexpected happened but didn't affected the operation process
 * {@code ERROR} - expected or unexpected exception was thrown
 * {@code FATAL} - incorrect service configuration or some of the dependencies didn't respond
 */
public enum MessageType {
    INFO,
    WARN,
    ERROR,
    FATAL
}
