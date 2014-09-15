package ru.forxy.fraud.exceptions;

import ru.forxy.common.exceptions.EventLogBase;
import ru.forxy.common.exceptions.EventType;

public enum FraudServiceEventLogId implements EventLogBase {

    // -------------------------------------------------------------------
    // Business events
    // -------------------------------------------------------------------
    IDShouldNotBeNull(FraudServiceEventLogId.BASE_EVENT_LOG_ID, 400,
            "Transaction id sould not be null.",
            EventType.InvalidInput),

    TransactionNotFound(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 1, 400,
            "Transaction with id %1$l is not found.",
            EventType.InvalidInput),

    InvalidPageNumber(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 2, 400,
            "Invalid page number provided: '%1$s'",
                      EventType.InvalidInput),

    IsInBlackListAlready(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 3, 400,
            "%1$s with value= '%2$s' is in black list already",
                      EventType.InvalidInput),

    BlackListItemIsNotExist(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 4, 400,
            "%1$s with value= '%2$s' is not exist",
            EventType.InvalidInput),

    VelocityConfigNotFound(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 5, 404,
            "VelocityConfig for metric '%1$s' is not found.",
            EventType.InvalidInput),

    VelocityConfigAlreadyExists(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 6, 400,
            "VelocityConfig for metric '%1$s' already exists.",
            EventType.InvalidInput),

    UnexpectedErrorDuringVelocityComputation(FraudServiceEventLogId.BASE_EVENT_LOG_ID + 7, 500,
            "Unexpected error during velocity metric '%1$s' computation.",
            EventType.InternalError);

    public static final int BASE_EVENT_LOG_ID = 20000;

    private Level m_logLevel;
    private String m_formatString;
    private int m_eventId;
    private int m_responseId;
    private EventType m_eventType;

    private FraudServiceEventLogId(final int eventId, final int responseId, final String formatString, final EventType eventType) {
        this(eventId, responseId, Level.ERROR, formatString, eventType);
    }

    private FraudServiceEventLogId(final int eventId, final int responseId, final Level level, final String formatString,
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