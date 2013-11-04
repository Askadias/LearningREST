package ru.forxy.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * Test servlet context attribute listener
 */
public class MyContextAttributeListener implements ServletContextAttributeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyContextAttributeListener.class);

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        LOGGER.info("Context Attribute '{}' with value '{}' added", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        LOGGER.info("Context Attribute '{}' with value '{}' removed", event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        LOGGER.info("Context Attribute '{}' value replaced with '{}'", event.getName(), event.getValue());
    }
}
