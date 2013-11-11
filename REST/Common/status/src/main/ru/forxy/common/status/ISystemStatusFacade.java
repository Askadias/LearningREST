package ru.forxy.common.status;

import ru.forxy.common.status.pojo.SystemStatus;

/**
 * Facade includes the business logic for service and its components health check
 */
public interface ISystemStatusFacade {

    SystemStatus getStatus();
}
