package ru.forxy.fraud.db.dao;

import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.pojo.list.BlackListItem;

import java.util.List;

/**
 * Data Access Object for fraud database to manipulate BlackLists.
 */
public interface IBlackListDAO extends ISystemStatusComponent {

    List<BlackListItem> getAll();

    List<BlackListItem> getMore(final BlackListItem start, final int limit);

    void save(final BlackListItem item);

    void delete(final BlackListItem item);
}

