package ru.forxy.fraud.rest.pojo;

import java.util.Map;

public abstract class Entity {

    protected String name;

    protected Map<String, Entity> nodes;

    protected Map<String, Object> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Entity> getNodes() {
        return nodes;
    }

    public void setNodes(Map<String, Entity> nodes) {
        this.nodes = nodes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
