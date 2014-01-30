package ru.forxy.fraud.db.dao.mongo;

import com.mongodb.CommandResult;
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
import ru.forxy.fraud.db.dao.IFraudDAO;
import ru.forxy.fraud.rest.pojo.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Mongo DB based data source for transactions
 */
public class FraudDAO implements IFraudDAO {

    MongoTemplate mongoTemplate;

    @Override
    public Iterable<Transaction> findAll(final Sort sort) {
        return mongoTemplate.find(Query.query(new Criteria()).with(sort), Transaction.class);
    }

    @Override
    public Page<Transaction> findAll(final Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = pageable.getOffset();
        List<Transaction> transactions = mongoTemplate.find(Query.query(new Criteria()).limit(size).skip(offset), Transaction.class);
        return new PageImpl<Transaction>(transactions, pageable, count());
    }

    @Override
    public <S extends Transaction> S save(final S transaction) {
        mongoTemplate.save(transaction);
        return transaction;
    }

    @Override
    public <S extends Transaction> Iterable<S> save(final Iterable<S> transactions) {
        throw null;
    }

    @Override
    public Transaction findOne(final Long id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Transaction.class);
    }

    @Override
    public boolean exists(final Long id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Transaction.class) != null;
    }

    @Override
    public Iterable<Transaction> findAll() {
        return mongoTemplate.findAll(Transaction.class);
    }

    @Override
    public Iterable<Transaction> findAll(final Iterable<Long> ids) {
        return mongoTemplate.find(Query.query(Criteria.where("id").in(ids)), Transaction.class);
    }

    @Override
    public long count() {
        return mongoTemplate.count(null, Transaction.class);
    }

    @Override
    public void delete(final Long id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Transaction.class);
    }

    @Override
    public void delete(final Transaction transaction) {
        mongoTemplate.remove(transaction);
    }

    @Override
    public void delete(final Iterable<? extends Transaction> transactions) {
        for (Transaction transaction : transactions) {
            mongoTemplate.remove(transaction);
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
                CommandResult lastError = mongoTemplate.getDb().getLastError();
                //noinspection ThrowableResultOfMethodCallIgnored
                if (lastError.getException() != null) {
                    exceptionMessage = lastError.getErrorMessage();
                    exceptionDetails = ExceptionUtils.getStackTrace(lastError.getException());
                    statusType = StatusType.YELLOW;
                }
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
        return new ComponentStatus("Fraud DAO", location, statusType, null, ComponentStatus.ComponentType.DB,
                responseTime, null, exceptionMessage, exceptionDetails);
    }
}
