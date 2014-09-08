package ru.forxy.fraud.rest.pojo.list;

import javax.persistence.EmbeddedId;
import javax.persistence.Table;

/**
 * Bypass item
 */
@Table(name = "whitelist")
public class WhiteListItem {
    private static final long serialVersionUID = -5883561489932974719L;

    @EmbeddedId
    private ListPartitionKey key;

    public ListPartitionKey getKey() {
        return key;
    }

    public void setKey(ListPartitionKey key) {
        this.key = key;
    }
}
