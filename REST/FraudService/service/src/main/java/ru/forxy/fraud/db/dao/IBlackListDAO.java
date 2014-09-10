package ru.forxy.fraud.db.dao;

import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.list.BlackListItem;
import ru.forxy.fraud.rest.v1.list.ListPartitionKey;

import java.util.List;

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
public interface IBlackListDAO extends ISystemStatusComponent {

    boolean isInBlackList(ListPartitionKey key);

    List<BlackListItem> getList(final String type, final String value, final int limit);

    BlackListItem get(final ListPartitionKey id);

    void save(final BlackListItem item);

    void delete(final BlackListItem item);
}

