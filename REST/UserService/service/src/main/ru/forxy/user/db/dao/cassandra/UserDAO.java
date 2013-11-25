package ru.forxy.user.db.dao.cassandra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.user.db.dao.IUserDAO;
import ru.forxy.user.rest.pojo.User;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Cassandra DB based data source for users
 */
public class UserDAO implements IUserDAO {

    EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findByLastName(String LastName) {
        return null;
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        return null;
    }

    @Override
    public ComponentStatus getStatus() {
        return null;
    }

    @Override
    public Iterable<User> findAll(Sort sort) {
        return entityManager.createQuery("select User from User").getResultList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <T extends User> T save(T entity) {
        return null;
    }

    @Override
    public <T extends User> Iterable<T> save(Iterable<T> entities) {
        return null;
    }

    @Override
    public User findOne(String s) {
        return null;
    }

    @Override
    public boolean exists(String s) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Iterable<User> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void delete(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
