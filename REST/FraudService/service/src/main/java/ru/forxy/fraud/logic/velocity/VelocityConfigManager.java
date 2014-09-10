package ru.forxy.fraud.logic.velocity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.fraud.db.dao.IVelocityConfigDAO;
import ru.forxy.fraud.exceptions.FraudServiceEventLogId;
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation class for VelocityConfigService business logic
 */
public class VelocityConfigManager implements IVelocityConfigManager {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IVelocityConfigDAO velocityConfigDAO;

    public List<VelocityConfig> getAllVelocityConfigs() {
        List<VelocityConfig> allVelocityConfigs = new LinkedList<>();
        for (VelocityConfig velocityConfig : velocityConfigDAO.findAll()) {
            allVelocityConfigs.add(velocityConfig);
        }
        return allVelocityConfigs;
    }

    @Override
    public EntityPage<VelocityConfig> getVelocityConfigs(final Integer page, final Integer size, final SortDirection sortDirection,
                                                         final String sortedBy, final VelocityConfig filter) {
        if (page >= 1) {
            int pageSize = size == null ? DEFAULT_PAGE_SIZE : size;
            PageRequest pageRequest;
            if (sortDirection != null && sortedBy != null) {
                Sort.Direction dir = sortDirection == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
                pageRequest = new PageRequest(page - 1, pageSize, dir, sortedBy);
            } else {
                pageRequest = new PageRequest(page - 1, pageSize);
            }
            final Page<VelocityConfig> p = velocityConfigDAO.findAll(pageRequest, filter);
            return new EntityPage<>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
        } else {
            throw new ServiceException(FraudServiceEventLogId.InvalidPageNumber, page);
        }
    }

    @Override
    public VelocityConfig getVelocityConfig(final String velocityConfigID) {
        VelocityConfig velocityConfig = velocityConfigDAO.findOne(velocityConfigID);
        if (velocityConfig == null) {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigNotFound, velocityConfigID);
        }
        return velocityConfig;
    }

    @Override
    public void updateVelocityConfig(final VelocityConfig velocityConfig) {
        if (velocityConfigDAO.exists(velocityConfig.getMetricType())) {
            velocityConfigDAO.save(velocityConfig);
        } else {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigNotFound, velocityConfig.getMetricType());
        }
    }

    @Override
    public void createVelocityConfig(final VelocityConfig velocityConfig) {
        velocityConfig.setCreateDate(new Date());
        if (!velocityConfigDAO.exists(velocityConfig.getMetricType())) {
            velocityConfigDAO.save(velocityConfig);
        } else {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigAlreadyExists, velocityConfig.getMetricType());
        }
    }

    @Override
    public void deleteVelocityConfig(final String velocityConfigID) {
        if (velocityConfigDAO.exists(velocityConfigID)) {
            velocityConfigDAO.delete(velocityConfigID);
        } else {
            throw new ServiceException(FraudServiceEventLogId.VelocityConfigNotFound, velocityConfigID);
        }
    }

    public void setVelocityConfigDAO(final IVelocityConfigDAO velocityConfigDAO) {
        this.velocityConfigDAO = velocityConfigDAO;
    }
}
