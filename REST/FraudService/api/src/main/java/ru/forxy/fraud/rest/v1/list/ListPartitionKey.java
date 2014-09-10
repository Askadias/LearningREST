package ru.forxy.fraud.rest.v1.list;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Base list item definition
 */
public class ListPartitionKey implements Serializable {

    private static final long serialVersionUID = 3445922380692875758L;

    @Column (name = "type")
    private String type;
    @Column (name = "value")
    private String value;

    public ListPartitionKey() {
    }

    public ListPartitionKey(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
