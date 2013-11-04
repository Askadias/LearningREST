package ru.forxy.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Uladzislau_Prykhodzk on 11/4/13.
 */
public class MySessionListener implements HttpSessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOGGER.info("Session '{}' created", se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOGGER.info("Session '{}' destroyed", se.getSession().getId());
    }
}
