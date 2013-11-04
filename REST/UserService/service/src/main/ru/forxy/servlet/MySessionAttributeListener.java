package ru.forxy.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Test session attribute listener
 */
public class MySessionAttributeListener implements HttpSessionAttributeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySessionAttributeListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        LOGGER.info("Session Attribute '{}' with value '{}' added", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        LOGGER.info("Session Attribute '{}' with value '{}' removed", event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        LOGGER.info("Session Attribute '{}' value replaced with '{}'", event.getName(), event.getValue());
    }
}
