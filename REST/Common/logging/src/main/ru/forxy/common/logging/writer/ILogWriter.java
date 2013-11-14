package ru.forxy.common.logging.writer;

import ru.forxy.common.support.Context;

/**
 * Can log the ContextData
 */
public interface ILogWriter {

    void log(Context.ContextData data);
}
