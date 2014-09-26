package fraud.db.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import common.status.ISystemStatusComponent
import fraud.rest.v1.velocity.VelocityConfig

/**
 * Data Access Object for Forxy database to manipulate VelocityConfigs.
 */
interface IVelocityConfigDAO extends PagingAndSortingRepository<VelocityConfig, String>, ISystemStatusComponent {

    Page<VelocityConfig> findAll(final Pageable pageable, final VelocityConfig filter)

    VelocityConfig saveConfig(final VelocityConfig velocityConfig);
}

