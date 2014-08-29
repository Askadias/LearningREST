package ru.forxy.auth.db.dao;

import org.springframework.data.repository.CrudRepository;
import ru.forxy.auth.rest.v1.pojo.Profile;
import ru.forxy.common.status.ISystemStatusComponent;

/**
 * Data Access Object for User database to manipulate Users.
 */
public interface IProfileDAO extends CrudRepository<Profile, String>, ISystemStatusComponent {
}

