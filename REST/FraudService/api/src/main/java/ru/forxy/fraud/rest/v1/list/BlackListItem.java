package ru.forxy.fraud.rest.v1.list;

import javax.persistence.Table;

/**
 * Denial item
 */
@Table(name = "blacklist")
public class BlackListItem extends BaseListItem {

    private static final long serialVersionUID = 8002490918713648931L;
}
