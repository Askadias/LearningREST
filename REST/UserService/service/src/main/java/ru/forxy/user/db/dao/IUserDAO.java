package ru.forxy.user.db.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.List;

/**
 * Data Access Object for User database to manipulate Users.
 */
public interface IUserDAO extends PagingAndSortingRepository<User, String>, ISystemStatusComponent {

    List<User> findByLastName(final String LastName);

    List<User> findByFirstName(final String firstName);
}

