package ru.forxy.user.exceptions;

import ru.forxy.common.exceptions.EventLogBase;
import ru.forxy.common.exceptions.EventType;
import ru.forxy.common.logging.exceptions.LoggingCommonEventLogId;

public enum UserServiceEventLogId implements EventLogBase {

    // -------------------------------------------------------------------
    // Business events
    // -------------------------------------------------------------------
    UserNotFound(LoggingCommonEventLogId.BASE_EVENT_LOG_ID, 500,
            "User with email '%1$s' is not found.",
            EventType.InternalError),

    UserAlreadyExists(LoggingCommonEventLogId.BASE_EVENT_LOG_ID + 1, 500,
            "User with email '%1$s' already exists.",
            EventType.InternalError),

    EmailIsNullOrEmpty(LoggingCommonEventLogId.BASE_EVENT_LOG_ID + 2, 500,
            "Requested user's email shouldn't be null or empty.",
            EventType.InternalError),

    EmptyLoginEmailOrPassword(LoggingCommonEventLogId.BASE_EVENT_LOG_ID + 100, 500,
            "To login user's email and password should present.",
            EventType.InternalError),

    // -------------------------------------------------------------------
    // DB events
    // -------------------------------------------------------------------
    CannotRetrieveCassandraSession(LoggingCommonEventLogId.BASE_EVENT_LOG_ID + 101, 500,
            "Cannot retrieve cassandra connection.",
            EventType.InternalError);

    public static final int BASE_EVENT_LOG_ID = 10000;

    private Level m_logLevel;
    private String m_formatString;
    private int m_eventId;
    private int m_responseId;
    private EventType m_eventType;

    private UserServiceEventLogId(final int eventId, final int responseId, final String formatString, final EventType eventType) {
        this(eventId, responseId, Level.ERROR, formatString, eventType);
    }

    private UserServiceEventLogId(final int eventId, final int responseId, final Level level, final String formatString,
                                  final EventType eventType) {
        m_eventId = eventId;
        m_responseId = responseId;
        m_logLevel = level;
        m_formatString = formatString;
        m_eventType = eventType;
    }

    @Override
    public int getEventId() {
        return m_eventId;
    }

    public int getResponseId() {
        return m_responseId;
    }

    @Override
    public Level getLogLevel() {
        return m_logLevel;
    }

    @Override
    public EventType getEventType() {
        return m_eventType;
    }

    public String getMessage(final Object... arguments) {
        return arguments != null && arguments.length > 0 ? String.format(m_formatString, arguments) : m_formatString;
    }
}