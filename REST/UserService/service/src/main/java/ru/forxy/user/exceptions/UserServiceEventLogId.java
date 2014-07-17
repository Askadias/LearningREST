package ru.forxy.user.exceptions;

import ru.forxy.common.exceptions.EventLogBase;
import ru.forxy.common.exceptions.EventType;
import ru.forxy.common.logging.exceptions.LoggingCommonEventLogId;

public enum UserServiceEventLogId implements EventLogBase {

    // -------------------------------------------------------------------
    // Business events
    // -------------------------------------------------------------------
    UserNotFound(UserServiceEventLogId.BASE_EVENT_LOG_ID, 404,
            "User with email '%1$s' is not found.",
            EventType.InvalidInput),

    UserAlreadyExists(UserServiceEventLogId.BASE_EVENT_LOG_ID + 1, 400,
            "User with email '%1$s' already exists.",
            EventType.InvalidInput),

    EmailIsNullOrEmpty(UserServiceEventLogId.BASE_EVENT_LOG_ID + 2, 400,
            "Requested user's email shouldn't be null or empty.",
            EventType.InvalidInput),

    EmptyLoginEmailOrPassword(UserServiceEventLogId.BASE_EVENT_LOG_ID + 3, 400,
            "To login user's email and password should present.",
            EventType.InvalidInput),

    InvalidPageNumber(UserServiceEventLogId.BASE_EVENT_LOG_ID + 4, 400,
            "Invalid page number provided: '%1$s'",
            EventType.InvalidInput),

    NotAuthorized(UserServiceEventLogId.BASE_EVENT_LOG_ID + 5, 401,
            "Invalid user name ('%1$s') or password", EventType.InvalidInput),


    ClientNotFound(UserServiceEventLogId.BASE_EVENT_LOG_ID + 6, 404,
            "Client with ID '%1$s' is not found.",
            EventType.InvalidInput),

    ClientAlreadyExists(UserServiceEventLogId.BASE_EVENT_LOG_ID + 7, 400,
            "Client with ID '%1$s' already exists.",
            EventType.InvalidInput),

    // -------------------------------------------------------------------
    // DB events
    // -------------------------------------------------------------------
    CannotRetrieveCassandraSession(LoggingCommonEventLogId.BASE_EVENT_LOG_ID + 100, 500,
            "Cannot retrieve cassandra connection.",
            EventType.InternalError);

    private static final int BASE_EVENT_LOG_ID = 10000;

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