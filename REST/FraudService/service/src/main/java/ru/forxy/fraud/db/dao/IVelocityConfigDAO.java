package ru.forxy.fraud.db.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.forxy.common.status.ISystemStatusComponent;
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig;

/**
 * Data Access Object for Forxy database to manipulate VelocityConfigs.
 */
public interface IVelocityConfigDAO extends PagingAndSortingRepository<VelocityConfig, String>, ISystemStatusComponent {

    Page<VelocityConfig> findAll(final Pageable pageable, final VelocityConfig filter);
}

