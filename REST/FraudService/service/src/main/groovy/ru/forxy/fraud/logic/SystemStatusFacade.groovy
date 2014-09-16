package ru.forxy.fraud.logic

import ru.forxy.common.status.ISystemStatusFacade
import ru.forxy.common.status.pojo.ComponentStatus
import ru.forxy.common.status.pojo.StatusType
import ru.forxy.common.status.pojo.SystemStatus
import ru.forxy.common.support.SystemProperties
import ru.forxy.fraud.db.dao.ITransactionDAO

/**
 * Business logic to prepare system status
 */
class SystemStatusFacade implements ISystemStatusFacade {

    ITransactionDAO transactionDAO

    @Override
    SystemStatus getStatus() {
        StatusType systemStatusType = null
        List<ComponentStatus> componentStatuses = []
        if (transactionDAO != null) {
            componentStatuses.add(transactionDAO.getStatus())
        } else {
            systemStatusType = StatusType.RED
        }

        // @formatter:off
        new SystemStatus(
                SystemProperties.getServiceName(),
                SystemProperties.getHostAddress(),
                SystemProperties.getServiceVersion(),
                systemStatusType != null ? systemStatusType : getTheWorstStatus(componentStatuses),
                componentStatuses)
        // @formatter:on
    }

    private static StatusType getTheWorstStatus(final List<ComponentStatus> componentStatuses) {
        StatusType theWorstStatus = StatusType.GREEN
        for (ComponentStatus componentStatus : componentStatuses) {
            if (componentStatus.getStatus().ordinal() > theWorstStatus.ordinal()) {
                theWorstStatus = componentStatus.getStatus()
            }
        }
        theWorstStatus
    }
}
