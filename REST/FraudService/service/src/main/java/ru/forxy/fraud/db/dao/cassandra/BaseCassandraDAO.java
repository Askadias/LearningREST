package ru.forxy.fraud.db.dao.cassandra;

import com.datastax.driver.mapping.MappingSession;

/**
 * Base Cassandra DAO
 */
public abstract class BaseCassandraDAO {

    CassandraSessionFactory sessionFactory;
    MappingSession mappingSession;

    public void setSessionFactory(CassandraSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setMappingSession(MappingSession mappingSession) {
        this.mappingSession = mappingSession;
    }
}
