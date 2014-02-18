package ru.forxy.fraud.dto;

import java.util.Map;

/**
 * Class contains the list of risk indicators
 */
public abstract class RiskyObject {
    private String type;
    private Map<String, String> attributes;
}
