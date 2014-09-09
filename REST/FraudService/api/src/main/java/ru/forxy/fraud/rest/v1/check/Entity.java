package ru.forxy.fraud.rest.v1.check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = -995088459065918416L;

    protected Map<String, Object> attributes = new HashMap<String, Object>();

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void putAttribute(final String name, final Object value) {
        attributes.put(name, value);
    }

    public void addAttributeListValue(final String name, final Object value) {
        Object listValue = attributes.get(name);
        if (listValue != null) {
            if (listValue instanceof Collection) {
                ((Collection) listValue).add(value);
            }
        } else {
            List<Object> list = new ArrayList<>();
            list.add(value);
            attributes.put(name, list);
        }
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
