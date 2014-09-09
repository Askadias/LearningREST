package ru.forxy.fraud.rest.v1.list;

import javax.persistence.Table;

/**
 * Bypass item
 */
@Table(name = "whitelist")
public class WhiteListItem extends BaseListItem {
    private static final long serialVersionUID = -5883561489932974719L;
}
