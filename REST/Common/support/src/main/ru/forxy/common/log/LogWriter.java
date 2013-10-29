package ru.forxy.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.utils.Context;

/**
 * Slf4j log writer implementation
 */
public class LogWriter implements ILogWriter {

    private Logger logger;

    public LogWriter() {
        logger = LoggerFactory.getLogger(LogWriter.class);
    }

    public LogWriter(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    @Override
    public void log(Context.ContextData data) {
        logger.info(String.valueOf(data));
    }
}
