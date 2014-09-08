package ru.forxy.fraud.rest.v1;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {

    protected Map<String, Object> attributes = new HashMap<String, Object>();

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
