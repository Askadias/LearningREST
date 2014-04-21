package ru.forxy.user.logic;

import ru.forxy.common.status.ISystemStatusFacade;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.common.status.pojo.StatusType;
import ru.forxy.common.status.pojo.SystemStatus;
import ru.forxy.common.support.SystemProperties;
import ru.forxy.user.db.dao.IUserDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic to prepare system status
 */
public class SystemStatusFacade implements ISystemStatusFacade {

    private IUserDAO userDAO;

    @Override
    public SystemStatus getStatus() {
        StatusType systemStatusType = null;
        List<ComponentStatus> componentStatuses = new ArrayList<ComponentStatus>();
        if (userDAO != null) {
            componentStatuses.add(userDAO.getStatus());
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

    public void setUserDAO(final IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
