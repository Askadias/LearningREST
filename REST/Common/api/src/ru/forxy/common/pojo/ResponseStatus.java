package ru.forxy.common.pojo;

/**
 * The overall service response status:
 * {@code SUCCESS} operation succeeded
 * {@code PARTIAL_SUCCESS} operation complete but some of it's steps weren't done (i.e. logging)
 * {$code FAIL} operation completely failed
 */
public enum ResponseStatus {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAIL
}
