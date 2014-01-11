package ru.forxy.common.logging.writer;

import ru.forxy.common.support.Context;

/**
 * Can log the ContextData
 */
public interface ILogWriter {

    void log(final Context.ContextData data);
}
