package ru.forxy.fraud.db.dao.cassandra;

import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.fraud.db.dao.IBlackListDAO;
import ru.forxy.fraud.rest.v1.list.BlackListItem;
import ru.forxy.fraud.rest.v1.list.ListPartitionKey;

import java.util.List;

/**
 * BlackLists DAO implementation
 */
public class BlackListDAO extends BaseCassandraDAO implements IBlackListDAO {

    @Override
    public boolean isInBlackList(ListPartitionKey key) {
        BlackListItem item = mappingSession.get(BlackListItem.class, key);
        return item != null && item.getIsActive();
    }

    @Override
    public List<BlackListItem> getList(final String type, final String value, int limit) {
        if (type != null && value != null) {
            return mappingSession.getByQuery(BlackListItem.class,
                    "select * from blacklist " +
                            "where token (type, value) > " +
                            "token('" + type + "','" + value + "') " +
                            "limit " + limit);
        } else {
            return mappingSession.getByQuery(BlackListItem.class,
                    "select * from blacklist limit " + limit);
        }
    }

    @Override
    public BlackListItem get(final ListPartitionKey id) {
        return mappingSession.get(BlackListItem.class, id);
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
