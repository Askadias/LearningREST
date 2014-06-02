package ru.forxy.user.db.dao.mongo;

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
import ru.forxy.user.db.dao.IUserDAO;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.Date;
import java.util.List;

/**
 * Mongo DB based data source for users
 */
public class UserDAO implements IUserDAO {

    MongoTemplate mongoTemplate;

    @Override
    public List<User> findByLastName(final String lastName) {
        return mongoTemplate.find(Query.query(Criteria.where("lastName").is(lastName)), User.class);
    }

    @Override
    public List<User> findByFirstName(final String firstName) {
        return mongoTemplate.find(Query.query(Criteria.where("firstName").is(firstName)), User.class);
    }

    @Override
    public Iterable<User> findAll(final Sort sort) {
        return mongoTemplate.find(Query.query(new Criteria()).with(sort), User.class);
    }

    @Override
    public Page<User> findAll(final Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = pageable.getOffset();
        List<User> users = mongoTemplate.find(Query.query(new Criteria()).limit(size).skip(offset), User.class);
        return new PageImpl<User>(users, pageable, count());
    }

    @Override
    public <S extends User> S save(final S user) {
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public <S extends User> Iterable<S> save(final Iterable<S> users) {
        throw null;
    }

    @Override
    public User findOne(final String email) {
        return mongoTemplate.findOne(Query.query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public boolean exists(final String email) {
        return mongoTemplate.findOne(Query.query(Criteria.where("email").is(email)), User.class) != null;
    }

    @Override
    public Iterable<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public Iterable<User> findAll(final Iterable<String> emails) {
        return mongoTemplate.find(Query.query(Criteria.where("email").in(emails)), User.class);
    }

    @Override
    public long count() {
        return mongoTemplate.count(null, User.class);
    }

    @Override
    public void delete(final String email) {
        mongoTemplate.remove(Query.query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public void delete(final User user) {
        mongoTemplate.remove(user);
    }

    @Override
    public void delete(final Iterable<? extends User> users) {
        for (User user : users) {
            mongoTemplate.remove(user);
        }
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(null, User.class);
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
                mongoTemplate.count(null, User.class);
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
        return new ComponentStatus("User DAO", location, statusType, null, ComponentStatus.ComponentType.DB,
                responseTime, null, exceptionMessage, exceptionDetails);
    }
}
