package ru.forxy.fraud.db.dao;

import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.list.BlackListItem;
import ru.forxy.fraud.rest.v1.list.ListPartitionKey;

import java.util.List;

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
public interface IBlackListDAO extends ISystemStatusComponent {

    List<BlackListItem> getAll();

    boolean isInBlackList(ListPartitionKey key);

    List<BlackListItem> getMore(final BlackListItem start, final int limit);

    BlackListItem get(final ListPartitionKey id);

    void save(final BlackListItem item);

    void delete(final BlackListItem item);
}

