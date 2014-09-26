package fraud.logic.velocity

import common.pojo.EntityPage
import common.pojo.SortDirection
import fraud.rest.v1.velocity.VelocityConfig

/**
 * Entry point into auth service business logic
 */
interface IVelocityConfigManager {

    Iterable<VelocityConfig> getAllVelocityConfigs()

    EntityPage<VelocityConfig> getVelocityConfigs(final Integer page, final Integer size, final SortDirection sortDirection,
                                                  final String sortedBy, final VelocityConfig filter)

    VelocityConfig getVelocityConfig(final String velocityConfigID)

    void updateVelocityConfig(final VelocityConfig auth)

    void createVelocityConfig(final VelocityConfig auth)

    void deleteVelocityConfig(final String velocityConfigID)
}
