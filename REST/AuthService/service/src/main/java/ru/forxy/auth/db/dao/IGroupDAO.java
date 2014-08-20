package ru.forxy.auth.db.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.forxy.auth.rest.v1.pojo.Client;
import ru.forxy.auth.rest.v1.pojo.Group;
import ru.forxy.common.status.ISystemStatusComponent;

/**
 * Data Access Object for Forxy database to manipulate Clients.
 */
public interface IGroupDAO extends PagingAndSortingRepository<Group, String>, ISystemStatusComponent {

    Page<Group> findAll(final Pageable pageable, final Group filter);
}

