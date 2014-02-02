package ru.forxy.fraud.rest.pojo;

import java.util.Map;

public abstract class Entity {

    protected String name;

    protected Map<String, Object> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}