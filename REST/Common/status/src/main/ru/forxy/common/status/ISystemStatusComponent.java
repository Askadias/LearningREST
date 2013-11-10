package ru.forxy.common.status;

import ru.forxy.common.status.api.ComponentStatus;

/**
 * Implementing this interface you should return health status of the implementor
 */
public interface ISystemStatusComponent {

    ComponentStatus getStatus();
}
