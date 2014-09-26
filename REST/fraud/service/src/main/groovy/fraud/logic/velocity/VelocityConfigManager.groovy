package fraud.logic.velocity

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import common.exceptions.ServiceException
import common.pojo.EntityPage
import common.pojo.SortDirection
import fraud.db.dao.IVelocityConfigDAO
import fraud.exceptions.FraudServiceEventLogId
import fraud.rest.v1.velocity.VelocityConfig

/**
 * Implementation class for VelocityConfigService business logic
 */
class VelocityConfigManager implements IVelocityConfigManager {

    static final int DEFAULT_PAGE_SIZE = 10

    IVelocityConfigDAO velocityConfigDAO

    List<VelocityConfig> getAllVelocityConfigs() {
        velocityConfigDAO.findAll().collect()
    }

    @Override
    EntityPage<VelocityConfig> getVelocityConfigs(
            final Integer page, final Integer size, final SortDirection sortDirection,
            final String sortedBy, final VelocityConfig filter) {
        if (page >= 1) {
            int pageSize = size == null ? DEFAULT_PAGE_SIZE : size
            PageRequest pageRequest
            if (sortDirection != null && sortedBy != null) {
                Sort.Direction dir = sortDirection == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC
                pageRequest = new PageRequest(page - 1, pageSize, dir, sortedBy)
            } else {
                pageRequest = new PageRequest(page - 1, pageSize)
            }
            final Page<VelocityConfig> p = velocityConfigDAO.findAll(pageRequest, filter)
            new EntityPage<>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements())
        } else {
            throw new ServiceException(FraudServiceEventLogId.InvalidPageNumber, page)
        }
    }

    @Override
    VelocityConfig getVelocityConfig(final String velocityConfigID) {
        VelocityConfig velocityConfig = velocityConfigDAO.findOne(velocityConfigID)
        if (velocityConfig == null) {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigNotFound, velocityConfigID)
        }
        velocityConfig
    }

    @Override
    void updateVelocityConfig(final VelocityConfig velocityConfig) {
        if (velocityConfigDAO.exists(velocityConfig.getMetricType())) {
            velocityConfigDAO.saveConfig(velocityConfig)
        } else {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigNotFound, velocityConfig.getMetricType())
        }
    }

    @Override
    void createVelocityConfig(final VelocityConfig velocityConfig) {
        velocityConfig.setCreateDate(new Date())
        if (!velocityConfigDAO.exists(velocityConfig.getMetricType())) {
            velocityConfigDAO.saveConfig(velocityConfig)
        } else {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigAlreadyExists, velocityConfig.getMetricType())
        }
    }

    @Override
    void deleteVelocityConfig(final String velocityConfigID) {
        if (velocityConfigDAO.exists(velocityConfigID)) {
            velocityConfigDAO.delete(velocityConfigID)
        } else {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigNotFound, velocityConfigID)
        }
    }
}
