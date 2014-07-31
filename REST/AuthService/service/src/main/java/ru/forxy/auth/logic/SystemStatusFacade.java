package ru.forxy.auth.logic;

import ru.forxy.auth.db.dao.IClientDAO;
import ru.forxy.auth.db.dao.ITokenDAO;
import ru.forxy.auth.db.dao.IUserDAO;
import ru.forxy.common.status.ISystemStatusFacade;
import ru.forxy.common.status.pojo.ComponentStatus;
import ru.forxy.common.status.pojo.StatusType;
import ru.forxy.common.status.pojo.SystemStatus;
import ru.forxy.common.support.SystemProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic to prepare system status
 */
public class SystemStatusFacade implements ISystemStatusFacade {

    private ITokenDAO tokenDAO;
    private IClientDAO clientDAO;
    private IUserDAO userDAO;

    @Override
    public SystemStatus getStatus() {
        StatusType systemStatusType = null;
        List<ComponentStatus> componentStatuses = new ArrayList<ComponentStatus>();
        if (tokenDAO != null && clientDAO != null && userDAO != null) {
            componentStatuses.add(tokenDAO.getStatus());
            componentStatuses.add(clientDAO.getStatus());
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

    public void setTokenDAO(final ITokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    public void setClientDAO(IClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
