package ru.forxy.fraud.db.dao.cassandra;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.EntityTypeParser;
import com.datastax.driver.mapping.meta.EntityFieldMetaData;
import com.datastax.driver.mapping.meta.EntityTypeMetadata;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.fraud.db.dao.IBlackListDAO;
import ru.forxy.fraud.rest.v1.list.BlackListItem;
import ru.forxy.fraud.rest.v1.list.ListPartitionKey;

import java.util.List;

import static com.datastax.driver.core.querybuilder.QueryBuilder.gt;
import static com.datastax.driver.core.querybuilder.QueryBuilder.token;

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
        EntityTypeMetadata emeta = EntityTypeParser.getEntityMetadata(BlackListItem.class);
        if (type != null && value != null) {
            EntityFieldMetaData typeMeta = emeta.getFieldMetadata("type");
            EntityFieldMetaData valueMeta = emeta.getFieldMetadata("value");
            return mappingSession.getByQuery(BlackListItem.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName())
                            .where(gt(token(typeMeta.getColumnName(), valueMeta.getColumnName()),
                                    token(type, value))).limit(limit));
        } else {
            return mappingSession.getByQuery(BlackListItem.class,
                    QueryBuilder.select().all().from(mappingSession.getKeyspace(), emeta.getTableName()).limit(limit));
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
