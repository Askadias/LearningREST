package ru.forxy.fraud.logic.velocity

import ru.forxy.common.pojo.EntityPage
import ru.forxy.common.pojo.SortDirection
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig

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
