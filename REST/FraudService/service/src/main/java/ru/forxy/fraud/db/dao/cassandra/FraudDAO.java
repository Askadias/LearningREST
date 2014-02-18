package ru.forxy.fraud.db.dao.cassandra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.common.status.pojo.StatusType;
import ru.forxy.fraud.db.dao.IFraudDAO;
import ru.forxy.fraud.db.dao.cassandra.client.ICassandraClient;
import ru.forxy.fraud.rest.pojo.Transaction;

import java.util.Date;

/**
 * Cassandra DB based data source for frauds
 */
public class FraudDAO implements IFraudDAO {

    private ICassandraClient<Transaction> cassandraClient;
	
    @Override
    public Iterable<Transaction> findAll(final Sort sort) {
        return cassandraClient.getAll();
    }

    @Override
    public Page<Transaction> findAll(final Pageable pageable) {
        return new PageImpl<Transaction>(cassandraClient.getAll());
    }

    @Override
    public <T extends Transaction> T save(final T transaction) {
        cassandraClient.add(transaction);
        return transaction;
    }

    @Override
    public <T extends Transaction> Iterable<T> save(final Iterable<T> entities) {
        for (final Transaction transaction : entities) {
            cassandraClient.add(transaction);
        }
        return entities;
    }

    @Override
    public Transaction findOne(final Long s) {
        return cassandraClient.getByKey(s);
    }

    @Override
    public boolean exists(final Long s) {
        return cassandraClient.existsKey(s);
    }

    @Override
    public Iterable<Transaction> findAll() {
        return cassandraClient.getAll();
    }

    @Override
    public Iterable<Transaction> findAll(final Iterable<Long> strings) {
        return cassandraClient.getAll();
    }

    @Override
    public long count() {
        return cassandraClient.count();
    }

    @Override
    public void delete(final Long s) {
        cassandraClient.deleteByKey(s);
    }

    @Override
    public void delete(final Transaction transaction) {
        cassandraClient.delete(transaction);
    }

    @Override
    public void delete(final Iterable<? extends Transaction> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public ComponentStatus getStatus() {
        return new ComponentStatus("Cassandra", "localhost", StatusType.YELLOW, null, ComponentStatus.ComponentType.DB,
                0, new Date(), null, null);
    }

    public void setCassandraClient(final ICassandraClient<Transaction> cassandraClient) {
        this.cassandraClient = cassandraClient;
    }
}
