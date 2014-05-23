package ru.forxy.common.exceptions;

/**
 * Base abstract exception class for all the Exceptions in the project
 * provides basic logic to handle error messages and status codes
 */
public abstract class BaseServiceException extends RuntimeException
{
    private final EventLogBase m_eventLogId;

    public BaseServiceException(final String message, final EventLogBase eventLogId)
    {
        super(message);
        m_eventLogId = eventLogId;
    }

    public BaseServiceException(final EventLogBase eventLogId)
    {
        super(eventLogId.getMessage());
        m_eventLogId = eventLogId;
    }

    public BaseServiceException(final Throwable cause, final String message, final EventLogBase eventLogId)
    {
        super(message, cause);
        m_eventLogId = eventLogId;
    }

    public BaseServiceException(final Throwable cause, final EventLogBase eventLogId)
    {
        super(eventLogId.getMessage(), cause);
        m_eventLogId = eventLogId;
    }

    public EventLogBase getEventLogId()
    {
        return m_eventLogId;
    }

    public int getStatusCode()
    {
        return m_eventLogId.getEventId();
    }
}
