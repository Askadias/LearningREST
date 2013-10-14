package ru.forxy.db.dao.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.user.pojo.User;

import java.util.List;

public class UserDAO implements IUserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    MongoTemplate mongoTemplate;

    @Override
    public List<User> findByLastName(String lastName) {
        return mongoTemplate.find(Query.query(Criteria.where("lastName").is(lastName)), User.class);
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return mongoTemplate.find(Query.query(Criteria.where("firstName").is(firstName)), User.class);
    }

    @Override
    public Iterable<User> findAll(Sort sort) {
        return mongoTemplate.find(Query.query(null).with(sort), User.class);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        List<User> users = mongoTemplate.find(Query.query(null).limit(pageable.getPageSize()).skip(pageable.getOffset()), User.class);
        return new PageImpl<User>(users);
    }

    @Override
    public <S extends User> S save(S user) {
        mongoTemplate.save(user);
        return user;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> users) {
        throw null;
    }

    @Override
    public User findOne(String email) {
        return mongoTemplate.findOne(Query.query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public boolean exists(String email) {
        return mongoTemplate.exists(Query.query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public Iterable<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public Iterable<User> findAll(Iterable<String> emails) {
        return mongoTemplate.find(Query.query(Criteria.where("email").in(emails)), User.class);
    }

    @Override
    public long count() {
        return mongoTemplate.count(null, User.class);
    }

    @Override
    public void delete(String email) {
        mongoTemplate.remove(Query.query(Criteria.where("email").is(email)), User.class);
    }

    @Override
    public void delete(User user) {
        mongoTemplate.remove(user);
    }

    @Override
    public void delete(Iterable<? extends User> users) {
        for (User user : users) {
            mongoTemplate.remove(user);
        }
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(null, User.class);
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
