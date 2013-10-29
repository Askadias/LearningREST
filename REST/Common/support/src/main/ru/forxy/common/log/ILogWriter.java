package ru.forxy.common.log;

import ru.forxy.common.utils.Context;

/**
 * Can log the ContextData
 */
public interface ILogWriter {
    void log(Context.ContextData data);
}
