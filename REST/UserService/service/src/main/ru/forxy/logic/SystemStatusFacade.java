package ru.forxy.logic;

import org.springframework.beans.factory.annotation.Autowired;
import ru.forxy.common.SystemProperties;
import ru.forxy.common.pojo.status.ComponentStatus;
import ru.forxy.common.pojo.status.StatusType;
import ru.forxy.common.pojo.status.SystemStatus;
import ru.forxy.common.status.ISystemStatusFacade;
import ru.forxy.db.dao.IUserDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic to prepare system status
 */
public class SystemStatusFacade implements ISystemStatusFacade {

    @Autowired
    IUserDAO userDAO;

    @Override
    public SystemStatus getStatus() {
        StatusType systemStatusType = null;
        List<ComponentStatus> componentStatuses = new ArrayList<ComponentStatus>();
        if (userDAO != null) {
            componentStatuses.add(userDAO.getStatus());
        } else {
            systemStatusType = StatusType.RED;
        }

        return new SystemStatus(
                SystemProperties.getServiceName(),
                SystemProperties.getHostAddress(),
                SystemProperties.getServiceVersion(),
                systemStatusType != null ? systemStatusType : getTheWorstStatus(componentStatuses),
                componentStatuses);
    }

    private StatusType getTheWorstStatus(List<ComponentStatus> componentStatuses) {
        StatusType theWorstStatus = StatusType.GREEN;
        for (ComponentStatus componentStatus : componentStatuses) {
            if (componentStatus.getStatus().ordinal() > theWorstStatus.ordinal()) {
                theWorstStatus = componentStatus.getStatus();
            }
        }
        return theWorstStatus;
    }
}
