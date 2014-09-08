package ru.forxy.fraud.rest.pojo.list;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 *  Base list item definition
 */
public class ListPartitionKey implements Serializable {

    private static final long serialVersionUID = 3445922380692875758L;
    @Id
    private String value;
    @Id
    private ListType type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ListType getType() {
        return type;
    }

    public void setType(ListType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListPartitionKey)) return false;

        ListPartitionKey listItem = (ListPartitionKey) o;

        if (type != listItem.type) return false;
        if (value != null ? !value.equals(listItem.value) : listItem.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
