package ru.forxy.user.db.dao.cassandra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.common.status.pojo.StatusType;
import ru.forxy.user.db.dao.IUserDAO;
import ru.forxy.user.rest.pojo.User;

import javax.persistence.EntityManager;
import java.util.Date;
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
    public Iterable<User> findAll(Sort sort) {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return new PageImpl<User>(entityManager.createQuery("select u from User u").getResultList());
    }

    @Override
    public <T extends User> T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <T extends User> Iterable<T> save(Iterable<T> entities) {
        return null;
    }

    @Override
    public User findOne(String s) {
        return entityManager.find(User.class, s);
    }

    @Override
    public boolean exists(String s) {
        return entityManager.contains(new User(s, null));
    }

    @Override
    public Iterable<User> findAll() {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Override
    public Iterable<User> findAll(Iterable<String> strings) {
        return entityManager.createQuery("select u from User u").getResultList();
    }

    @Override
    public long count() {
        return (Long) entityManager.createQuery("select count(u) from User u").getSingleResult();
    }

    @Override
    public void delete(String s) {
        entityManager.createQuery("delete from User u where u.email = " + s).executeUpdate();
    }

    @Override
    public void delete(User entity) {
        entityManager.detach(entity);
    }

    @Override
    public void delete(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public ComponentStatus getStatus() {
        return new ComponentStatus("Cassandra", "localhost", StatusType.YELLOW, null, ComponentStatus.ComponentType.DB, 0, new Date(), null, null);
    }
}
