package ru.forxy.fraud.rest.v1.list;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Base list item definition
 */
class ListPartitionKey implements Serializable {
    @Column (name = "type")
    String type;
    @Column (name = "value")
    String value;
}
