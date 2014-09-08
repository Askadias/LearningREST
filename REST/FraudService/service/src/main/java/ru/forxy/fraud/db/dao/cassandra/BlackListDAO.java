package ru.forxy.fraud.db.dao.cassandra;

import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.fraud.db.dao.IBlackListDAO;
import ru.forxy.fraud.rest.v1.list.BlackListItem;

import java.util.List;

/**
 * BlackLists DAO implementation
 */
public class BlackListDAO extends BaseCassandraDAO implements IBlackListDAO {

    @Override
    public List<BlackListItem> getAll() {
        return mappingSession.getByQuery(BlackListItem.class, "select value, type from blacklist");
    }

    @Override
    public List<BlackListItem> getMore(BlackListItem start, int limit) {
        return mappingSession.getByQuery(BlackListItem.class,
                "select value, type from blacklist " +
                        "where token (value, type) > " +
                        "token(" + start.getKey().getValue() + "," + start.getKey().getType() + ") " +
                        "limit " + limit);
    }

    @Override
    public void save(BlackListItem item) {
        mappingSession.save(item);
    }

    @Override
    public void delete(BlackListItem item) {
        mappingSession.delete(item);
    }

    @Override
    public ComponentStatus getStatus() {
        return new ComponentStatus();
    }
}
