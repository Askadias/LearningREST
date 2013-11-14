package ru.forxy.common.logging.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.common.support.Context;

/**
 * Slf4j-based log writer implementation
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
