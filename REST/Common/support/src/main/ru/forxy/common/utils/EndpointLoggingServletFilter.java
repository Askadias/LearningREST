package ru.forxy.common.utils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Askadias
 * Date: 27.10.13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
public class EndpointLoggingServletFilter implements Filter
{
	private Configuration configuration = new Configuration();

	private ILogWriter requestWriter;

	private ILogWriter responseWriter;

	private ILogWriter requestPayloadWriter;

	private ILogWriter responsePayloadWriter;

	private List<IFieldExtractorEndpoint> requestFieldExtractors;

	private List<IFieldExtractorEndpoint> responseFieldExtractors;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException
	{
		final HttpServletRequest rq = (HttpServletRequest) request;
		final HttpServletResponse rs = (HttpServletResponse) response;
		final String url = getRequestUrl(rq);
		if (configuration.getEnabled() && allowedUrl(url, configuration.getSkipUrls(), rq))
		{
			final long timestampStart = System.currentTimeMillis();
			final long timestampStartNano = System.nanoTime();
			final BufferedRequestWrapper brq = new BufferedRequestWrapper(rq,
					configuration.getPayloadMaxBufferSizeBytes());
			final BufferedResponseWrapper brs = new BufferedResponseWrapper(rs,
					configuration.getPayloadMaxBufferSizeBytes());

			Context.push();
			try
			{
				//handle request, add request data to context
				handleRequest(brq, timestampStart, url);
				//chain downstream
				chain.doFilter(brq, brs);
			}
			catch (IOException io)
			{
				handleError(io);
				throw io;
			}
			catch (ServletException se)
			{
				handleError(se);
				throw se;
			}
			catch (Exception e)
			{
				handleError(e);
				throw new ServletException(e);
			}
			finally
			{
				//handle response, add response data to context
				handleResponse(brq, brs, timestampStart, timestampStartNano);
				Context.pop();
			}
		}
		else
		{
			chain.doFilter(rq, rs);
		}
	}

	private void handleRequest(final BufferedRequestWrapper rq, final long timestampStart, final String url)
	{
		Context.addGlobal(Fields.ProductName, configuration.getProductName());
		Context.addGlobal(Fields.ActivityGUID, UUID.randomUUID().toString());
		Context.addFrame(Fields.ActivityName, configuration.getActivityName());
		Context.addFrame(Fields.ActivityStep, Fields.Values.rq);
		Context.addFrame(Fields.TimestampStart, new Date(timestampStart));
		Context.addFrame(Fields.Timestamp, new Date(timestampStart));
		Context.addFrame(Fields.HostLocal, MetadataHelper.getLocalHostAddress());
		Context.addFrame(Fields.HostRemote, rq.getRemoteAddr());

		if (requestFieldExtractors != null
			|| configuration.getEnabledHttpInfo()
			|| configuration.getEnabledPayload())
		{
			final Map<String, List<String>> rqHeaders = getHeaderMap(rq);
			//capture request http details
			if (configuration.getEnabledHttpInfo())
			{
				Context.addFrame(Fields.RequestURL, url);
				Context.addFrame(Fields.RequestMethod, rq.getMethod());
				Context.addFrame(Fields.RequestHeaders, rqHeaders);
			}
			//extract request custom fields from payload
			final byte[] payload = rq.getRequestBody();
			if (requestFieldExtractors != null)
			{
				for (final IFieldExtractorEndpoint fe : requestFieldExtractors)
				{
					final Map<String, Object> frame = Context.peek().getFrame();
					final Map<String, Object> extracted = fe.extract(payload, frame, rq, null, rqHeaders, null);
					frame.putAll(extracted);
				}
			}
			//capture request payload
			if (configuration.getEnabledPayload())
			{
				Context.addFrame(Fields.RequestPayload, payload);
				writeFrame(requestPayloadWriter);
			}
		}
		//write request frame
		writeFrame(requestWriter);
	}

	private void handleResponse(final BufferedRequestWrapper rq, final BufferedResponseWrapper rs,
								final long timestampStart, final long timestampStartNano)
	{
		final long timestampEnd = System.currentTimeMillis();
		final long timestampEndNano = System.nanoTime();
		Context.addFrame(Fields.ActivityStep, Fields.Values.rs);
		Context.addFrame(Fields.Timestamp, new Date(timestampEnd));
		Context.addFrame(Fields.TimestampEnd, new Date(timestampEnd));
		Context.addFrame(Fields.Duration, timestampEnd - timestampStart);
		Context.addFrame(Fields.DurationN, (timestampEndNano - timestampStartNano) / 1000000);
		//capture response http details
		if (configuration.getEnabledHttpInfo())
		{
			Context.addFrame(Fields.ResponseStatus, rs.getResponseStatus());
			Context.addFrame(Fields.ResponseURL, rs.getResponseRedirectURL());
			Context.addFrame(Fields.ResponseHeaders, rs.getResponseHeaders());
		}
		if (configuration.getEnabledPayload() || responseFieldExtractors != null)
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
			if (configuration.getEnabledPayload())
			{
				Context.addFrame(Fields.ResponsePayload, payload);
				writeFrame(responsePayloadWriter);
			}
		}
		writeFrame(responseWriter);
	}

	private static void writeFrame(final ILogWriter writer)
	{
		if (writer != null)
		{
			writer.log(Context.peek());
		}
	}

	private static void handleError(final Exception th)
	{
		if (!Context.contains(Fields.StatusCode))
		{
			Context.addFrame(Fields.StatusCode, MetadataHelper.getShortErrorDescription(th));
		}
	}

	private static Map<String, List<String>> getHeaderMap(final HttpServletRequest rq)
	{
		Map<String, List<String>> result = null;
		final Enumeration names = rq.getHeaderNames();
		if (names != null && names.hasMoreElements())
		{
			result = new LinkedHashMap<String, List<String>>();
			while (names.hasMoreElements())
			{
				final String name = (String) names.nextElement();
				@SuppressWarnings("unchecked")
				final Enumeration<String> values = rq.getHeaders(name);
				if (values != null)
				{
					result.put(name, Collections.list(values));
				}
			}
		}
		return result;
	}

	private static String getRequestUrl(final HttpServletRequest rq)
	{
		final StringBuilder url = new StringBuilder(256).append(rq.getRequestURL());
		if (rq.getQueryString() != null)
		{
			url.append("?").append(rq.getQueryString());
		}
		return url.toString();
	}

	private static boolean allowedUrl(String url, final String[] skip, final HttpServletRequest rq)
	{
		//for now we only filter GETs
		if (skip != null && url != null && "GET".equals(rq.getMethod()))
		{
			url = url.toLowerCase(Locale.US);
			for (final String item : skip)
			{
				if (url.contains(item.toLowerCase(Locale.US)))
				{
					return false;
				}
			}
		}
		return true;
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

	public void setRequestPayloadWriter(final ILogWriter requestPayloadWriter)
	{
		this.requestPayloadWriter = requestPayloadWriter;
	}

	public void setResponsePayloadWriter(final ILogWriter responsePayloadWriter)
	{
		this.responsePayloadWriter = responsePayloadWriter;
	}

	public void setRequestFieldExtractors(final List<IFieldExtractorEndpoint> requestFieldExtractors)
	{
		this.requestFieldExtractors = requestFieldExtractors;
	}

	public void setResponseFieldExtractors(final List<IFieldExtractorEndpoint> responseFieldExtractors)
	{
		this.responseFieldExtractors = responseFieldExtractors;
	}

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException
	{
		//unused
	}

	@Override
	public void destroy()
	{
		//unused
	}
}