package ru.forxy.common.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import ru.forxy.common.logging.support.Fields;
import ru.forxy.common.logging.support.MetadataHelper;
import ru.forxy.common.logging.writer.ILogWriter;
import ru.forxy.common.utils.Configuration;
import ru.forxy.common.utils.Context;
import ru.forxy.common.utils.SystemProperties;

import java.util.Date;
import java.util.UUID;

/**
 * Method interceptor for payload and performance logging
 */
public class LoggingInterceptor implements MethodInterceptor
{
    private Configuration configuration;

    private ILogWriter requestWriter;

    private ILogWriter responseWriter;

    private boolean useConfiguredActivityName = false;

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable
    {
        if (configuration.isPerformanceLoggingEnabled())
        {
            final long timestampStart = System.currentTimeMillis();
            final long timestampStartNano = System.nanoTime();
            Context.push();
            try
            {
                if (!Context.contains(Fields.ProductName))
                {
                    Context.addGlobal(Fields.ProductName, SystemProperties.getServiceName());
                }
                if (!Context.contains(Fields.ActivityGUID))
                {
                    Context.addGlobal(Fields.ActivityGUID, UUID.randomUUID().toString());
                }
                Context.addFrame(Fields.ActivityName, useConfiguredActivityName ? configuration.get(Fields.ActivityName)
                        : MetadataHelper.getRealClassName(invocation.getThis()));
                Context.addFrame(Fields.ActivityStep, Fields.Values.rq);
                Context.addFrame(Fields.TimestampStart, new Date(timestampStart));
                Context.addFrame(Fields.Timestamp, new Date(timestampStart));
                if (requestWriter != null)
                {
                    requestWriter.log(Context.peek());
                }
                return invocation.proceed();
            }
            catch (final Exception e)
            {
                if (!Context.contains(Fields.StatusCode))
                {
                    Context.addFrame(Fields.StatusCode, MetadataHelper.getShortErrorDescription(e));
                }
                throw e;
            }
            finally
            {
                final long timestampEndNano = System.nanoTime();
                final long timestampEnd = System.currentTimeMillis();
                Context.addFrame(Fields.ActivityStep, Fields.Values.rs);
                if (!Context.contains(Fields.OperationName))
                {
                    Context.addFrame(Fields.OperationName, invocation.getMethod().getName());
                }
                Context.addFrame(Fields.Timestamp, new Date(timestampEnd));
                Context.addFrame(Fields.TimestampEnd, new Date(timestampEnd));
                Context.addFrame(Fields.Duration, timestampEnd - timestampStart);
                Context.addFrame(Fields.DurationN, (timestampEndNano - timestampStartNano) / 1000000);
                if (responseWriter != null)
                {
                    responseWriter.log(Context.peek());
                }
                Context.pop();
            }
        }
        else
        {
            return invocation.proceed();
        }
    }

    public void setConfiguration(final Configuration configuration)
    {
        this.configuration = configuration;
    }

    public void setRequestWriter(final ILogWriter requestWriter)
    {
        this.requestWriter = requestWriter;
    }

    public void setResponseWriter(final ILogWriter responseWriter)
    {
        this.responseWriter = responseWriter;
    }

    public void setUseConfiguredActivityName(final boolean useConfiguredActivityName)
    {
        this.useConfiguredActivityName = useConfiguredActivityName;
    }
}