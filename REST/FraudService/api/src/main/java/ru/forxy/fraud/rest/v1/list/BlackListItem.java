package ru.forxy.fraud.rest.v1.list;

import javax.persistence.EmbeddedId;
import javax.persistence.Table;

/**
 * Denial item
 */
@Table(name = "blacklist")
public class BlackListItem {
    private static final long serialVersionUID = 8002490918713648931L;

    @EmbeddedId
    private ListPartitionKey key;

    public ListPartitionKey getKey() {
        return key;
    }

    public void setKey(ListPartitionKey key) {
        this.key = key;
    }
}
