package ru.forxy.fraud.rest.v1.list;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Bypass item
 */
@Entity
@Table(name = "whitelist")
public class WhiteListItem extends BaseListItem {

    private static final long serialVersionUID = -5883561489932974719L;

    public WhiteListItem() {
        super();
    }

    public WhiteListItem(String type, String value) {
        super(type, value);
    }
}
