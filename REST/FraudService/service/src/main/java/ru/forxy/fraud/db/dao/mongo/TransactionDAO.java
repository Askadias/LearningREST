package ru.forxy.fraud.db.dao.mongo;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.common.status.pojo.StatusType;
import ru.forxy.fraud.db.dao.ITransactionDAO;
import ru.forxy.fraud.rest.v1.check.Transaction;

import java.util.Date;

/**
 * Mongo DB based data source for auths
 */
public class TransactionDAO implements ITransactionDAO {

    private MongoTemplate mongoTemplate;

    @Override
    public Iterable<Transaction> findAll(final Sort sort) {
        return mongoTemplate.find(Query.query(new Criteria()).with(sort), Transaction.class);
    }

    @Override
    public Page<Transaction> findAll(final Pageable pageable) {
        return new PageImpl<>(
                mongoTemplate.find(Query.query(new Criteria()).with(pageable), Transaction.class),
                pageable, count()
        );
    }

    @Override
    public Page<Transaction> findAll(final Pageable pageable, final Transaction filter) {
        Query query = Query.query(new Criteria()).with(pageable);
        /*if (filter != null) {
            if (StringUtils.isNotEmpty(filter.getID())) {
                query.addCriteria(new Criteria("TransactionID").regex(filter.getTransactionID(), "i"));
            }
            if (StringUtils.isNotEmpty(filter.getName())) {
                query.addCriteria(new Criteria("name").regex(filter.getName(), "i"));
            }
            if (StringUtils.isNotEmpty(filter.getUpdatedBy())) {
                query.addCriteria(new Criteria("updatedBy").regex(filter.getUpdatedBy(), "i"));
            }
            if (StringUtils.isNotEmpty(filter.getCreatedBy())) {
                query.addCriteria(new Criteria("createdBy").regex(filter.getCreatedBy(), "i"));
            }
        }*/

        return new PageImpl<>(mongoTemplate.find(query, Transaction.class), pageable, count());
    }

    @Override
    public <S extends Transaction> S save(final S Transaction) {
        mongoTemplate.save(Transaction);
        return Transaction;
    }

    @Override
    public <S extends Transaction> Iterable<S> save(final Iterable<S> Transactions) {
        throw null;
    }

    @Override
    public Transaction findOne(final String TransactionID) {
        return mongoTemplate.findOne(Query.query(Criteria.where("TransactionID").is(TransactionID)), Transaction.class);
    }

    @Override
    public boolean exists(final String TransactionID) {
        return mongoTemplate.findOne(Query.query(Criteria.where("TransactionID").is(TransactionID)), Transaction.class) != null;
    }

    @Override
    public Iterable<Transaction> findAll() {
        return mongoTemplate.findAll(Transaction.class);
    }

    @Override
    public Iterable<Transaction> findAll(final Iterable<String> TransactionIDs) {
        return mongoTemplate.find(Query.query(Criteria.where("TransactionID").in(TransactionIDs)), Transaction.class);
    }

    @Override
    public long count() {
        return mongoTemplate.count(null, Transaction.class);
    }

    @Override
    public void delete(final String TransactionID) {
        mongoTemplate.remove(Query.query(Criteria.where("TransactionID").is(TransactionID)), Transaction.class);
    }

    @Override
    public void delete(final Transaction Transaction) {
        mongoTemplate.remove(Transaction);
    }

    @Override
    public void delete(final Iterable<? extends Transaction> Transactions) {
        for (Transaction Transaction : Transactions) {
            mongoTemplate.remove(Transaction);
        }
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(null, Transaction.class);
    }

    public void setMongoTemplate(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ComponentStatus getStatus() {
        String location = null;
        StatusType statusType = StatusType.GREEN;
        long responseTime = Long.MAX_VALUE;
        String exceptionMessage = null;
        String exceptionDetails = null;
        if (mongoTemplate != null && mongoTemplate.getDb() != null && mongoTemplate.getDb().getMongo() != null) {
            location = mongoTemplate.getDb().getMongo().getConnectPoint();

            long timeStart = new Date().getTime();
            try {
                mongoTemplate.count(null, Transaction.class);
            } catch (final Exception e) {
                exceptionMessage = e.getMessage();
                exceptionDetails = ExceptionUtils.getStackTrace(e);
                statusType = StatusType.RED;
            } finally {
                responseTime = new Date().getTime() - timeStart;
            }


        } else {
            statusType = StatusType.RED;
        }
        return new ComponentStatus("Transaction DAO", location, statusType, null, ComponentStatus.ComponentType.DB,
                responseTime, null, exceptionMessage, exceptionDetails);
    }
}
