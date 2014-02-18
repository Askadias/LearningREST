package ru.forxy.fraud.logic;

import ru.forxy.common.status.ISystemStatusFacade;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.common.status.pojo.StatusType;
import ru.forxy.common.status.pojo.SystemStatus;
import ru.forxy.common.support.SystemProperties;
import ru.forxy.fraud.db.dao.IFraudDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic to prepare system status
 */
public class SystemStatusFacade implements ISystemStatusFacade {

    private IFraudDAO fraudDAO;

    @Override
    public SystemStatus getStatus() {
        StatusType systemStatusType = null;
        List<ComponentStatus> componentStatuses = new ArrayList<ComponentStatus>();
        if (fraudDAO != null) {
            componentStatuses.add(fraudDAO.getStatus());
        } else {
            systemStatusType = StatusType.RED;
        }

        // @formatter:off
        return new SystemStatus(
                SystemProperties.getServiceName(),
                SystemProperties.getHostAddress(),
                SystemProperties.getServiceVersion(),
                systemStatusType != null ? systemStatusType : getTheWorstStatus(componentStatuses),
                componentStatuses);
        // @formatter:on
    }

    private StatusType getTheWorstStatus(final List<ComponentStatus> componentStatuses) {
        StatusType theWorstStatus = StatusType.GREEN;
        for (ComponentStatus componentStatus : componentStatuses) {
            if (componentStatus.getStatus().ordinal() > theWorstStatus.ordinal()) {
                theWorstStatus = componentStatus.getStatus();
            }
        }
        return theWorstStatus;
    }

    public void setFraudDAO(final IFraudDAO fraudDAO) {
        this.fraudDAO = fraudDAO;
    }
}
