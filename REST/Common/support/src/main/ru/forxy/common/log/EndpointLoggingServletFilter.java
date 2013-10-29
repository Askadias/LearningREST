package ru.forxy.common.log;

import ru.forxy.common.SystemProperties;
import ru.forxy.common.utils.Configuration;
import ru.forxy.common.utils.Context;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Request/Response logging filter
 */
public class EndpointLoggingServletFilter implements Filter {

    private final static int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private String activityName;

    private Configuration configuration;

    private ILogWriter requestWriter;

    private ILogWriter responseWriter;

    private ILogWriter requestPayloadWriter;

    private ILogWriter responsePayloadWriter;

	/*private List<IFieldExtractorEndpoint> requestFieldExtractors;

	private List<IFieldExtractorEndpoint> responseFieldExtractors;*/

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest rq = (HttpServletRequest) request;
        final HttpServletResponse rs = (HttpServletResponse) response;
        final String url = getRequestUrl(rq);
        if (configuration.isPerformanceLoggingEnabled()) {
            final long timestampStart = System.currentTimeMillis();
            final long timestampStartNano = System.nanoTime();
            final HttpServletRequestWrapper rqw = new BufferedRequestWrapper(rq, DEFAULT_BUFFER_SIZE);
            final HttpServletResponseWrapper rsw = new HttpServletResponseWrapper(rs);
            /*final BufferedRequestWrapper brq = new BufferedRequestWrapper(rq,
                    configuration.getPayloadMaxBufferSizeBytes());
			final BufferedResponseWrapper brs = new BufferedResponseWrapper(rs,
					configuration.getPayloadMaxBufferSizeBytes());*/

            Context.push();
            try {
                //handle request, add request data to context
                handleRequest(rqw, timestampStart, url);
                //chain downstream
                chain.doFilter(rqw, rsw);
            } catch (IOException io) {
                handleError(io);
                throw io;
            } catch (ServletException se) {
                handleError(se);
                throw se;
            } catch (Exception e) {
                handleError(e);
                throw new ServletException(e);
            } finally {
                //handle response, add response data to context
                handleResponse(rqw, rsw, timestampStart, timestampStartNano);
                Context.pop();
            }
        } else {
            chain.doFilter(rq, rs);
        }
    }

    private void handleRequest(final HttpServletRequestWrapper rq, final long timestampStart, final String url) {
        Context.addGlobal(Fields.ProductName, SystemProperties.getServiceName());//configuration.getProductName());
        Context.addGlobal(Fields.ActivityGUID, UUID.randomUUID().toString());
        Context.addFrame(Fields.ActivityName, activityName);//configuration.getActivityName());
        Context.addFrame(Fields.ActivityStep, Fields.Values.rq);
        Context.addFrame(Fields.TimestampStart, new Date(timestampStart));
        Context.addFrame(Fields.Timestamp, new Date(timestampStart));
        Context.addFrame(Fields.HostLocal, SystemProperties.getHostAddress());//MetadataHelper.getLocalHostAddress());
        Context.addFrame(Fields.HostRemote, rq.getRemoteAddr());

		/*if (requestFieldExtractors != null
			|| configuration.isHttpInfoLoggingEnabled()
			|| configuration.isPayloadLoggingEnabled())
		{
			final Map<String, List<String>> rqHeaders = getHeaderMap(rq);
			//capture request http details
			if (configuration.isHttpInfoLoggingEnabled())
			{
				Context.addFrame(Fields.RequestURL, url);
				Context.addFrame(Fields.RequestMethod, rq.getMethod());
				Context.addFrame(Fields.RequestHeaders, rqHeaders);
			}
			//extract request custom fields from payload
			final byte[] payload = rq.getRequestBody();
			*//*if (requestFieldExtractors != null)
			{
				for (final IFieldExtractorEndpoint fe : requestFieldExtractors)
				{
					final Map<String, Object> frame = Context.peek().getFrame();
					final Map<String, Object> extracted = fe.extract(payload, frame, rq, null, rqHeaders, null);
					frame.putAll(extracted);
				}
			}*//*
			//capture request payload
			if (configuration.isPayloadLoggingEnabled())
			{
				Context.addFrame(Fields.RequestPayload, payload);
				writeFrame(requestPayloadWriter);
			}
		}*/
        //write request frame
        writeFrame(requestWriter);
    }

    private void handleResponse(final HttpServletRequestWrapper rq, final HttpServletResponseWrapper rs,
                                final long timestampStart, final long timestampStartNano) {
        final long timestampEnd = System.currentTimeMillis();
        final long timestampEndNano = System.nanoTime();
        Context.addFrame(Fields.ActivityStep, Fields.Values.rs);
        Context.addFrame(Fields.Timestamp, new Date(timestampEnd));
        Context.addFrame(Fields.TimestampEnd, new Date(timestampEnd));
        Context.addFrame(Fields.Duration, timestampEnd - timestampStart);
        Context.addFrame(Fields.DurationN, (timestampEndNano - timestampStartNano) / 1000000);
        //capture response http details
		/*if (configuration.isHttpInfoLoggingEnabled())
		{
			Context.addFrame(Fields.ResponseStatus, rs.getResponseStatus());
			Context.addFrame(Fields.ResponseURL, rs.getResponseRedirectURL());
			Context.addFrame(Fields.ResponseHeaders, rs.getResponseHeaders());
		}
		if (configuration.isPayloadLoggingEnabled() || responseFieldExtractors != null)
		{
			final byte[] payload = rs.getResponseBody();
			//extract response custom fields from payload
			if (responseFieldExtractors != null)
			{
				for (final IFieldExtractorEndpoint fe : responseFieldExtractors)
				{
					final Map<String, Object> frame = Context.peek().getFrame();
					final Map<String, Object> extracted = fe.extract(payload, frame,
							rq, rs, getHeaderMap(rq), rs.getResponseHeaders());
					frame.putAll(extracted);
				}
			}
			//capture response payload
			if (configuration.isPayloadLoggingEnabled())
			{
				Context.addFrame(Fields.ResponsePayload, payload);
				writeFrame(responsePayloadWriter);
			}
		}*/
        writeFrame(responseWriter);
    }

    private static void writeFrame(final ILogWriter writer) {
        if (writer != null) {
            writer.log(Context.peek());
        }
    }

    private static void handleError(final Exception th) {
        if (!Context.contains(Fields.StatusCode)) {
            Context.addFrame(Fields.StatusCode, th.getMessage());//MetadataHelper.getShortErrorDescription(th));
        }
    }

    private static Map<String, List<String>> getHeaderMap(final HttpServletRequest rq) {
        Map<String, List<String>> result = null;
        final Enumeration names = rq.getHeaderNames();
        if (names != null && names.hasMoreElements()) {
            result = new LinkedHashMap<String, List<String>>();
            while (names.hasMoreElements()) {
                final String name = (String) names.nextElement();
                @SuppressWarnings("unchecked")
                final Enumeration<String> values = rq.getHeaders(name);
                if (values != null) {
                    result.put(name, Collections.list(values));
                }
            }
        }
        return result;
    }

    private static String getRequestUrl(final HttpServletRequest rq) {
        final StringBuilder url = new StringBuilder(256).append(rq.getRequestURL());
        if (rq.getQueryString() != null) {
            url.append("?").append(rq.getQueryString());
        }
        return url.toString();
    }

    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }

    public void setRequestWriter(final ILogWriter requestWriter) {
        this.requestWriter = requestWriter;
    }

    public void setResponseWriter(final ILogWriter responseWriter) {
        this.responseWriter = responseWriter;
    }

    public void setRequestPayloadWriter(final ILogWriter requestPayloadWriter) {
        this.requestPayloadWriter = requestPayloadWriter;
    }

    public void setResponsePayloadWriter(final ILogWriter responsePayloadWriter) {
        this.responsePayloadWriter = responsePayloadWriter;
    }

	/*public void setRequestFieldExtractors(final List<IFieldExtractorEndpoint> requestFieldExtractors)
	{
		this.requestFieldExtractors = requestFieldExtractors;
	}

	public void setResponseFieldExtractors(final List<IFieldExtractorEndpoint> responseFieldExtractors)
	{
		this.responseFieldExtractors = responseFieldExtractors;
	}*/

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}