package ru.forxy.fraud.db.dao.cassandra;

import com.datastax.driver.mapping.MappingSession;

/**
 * Base Cassandra DAO
 */
public abstract class BaseCassandraDAO {

    MappingSession mappingSession;

    public void setMappingSession(MappingSession mappingSession) {
        this.mappingSession = mappingSession;
    }
}
