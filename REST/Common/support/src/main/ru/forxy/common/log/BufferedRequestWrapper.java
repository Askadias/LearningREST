package ru.forxy.common.log;

import ru.forxy.common.utils.EncodingHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class BufferedRequestWrapper extends HttpServletRequestWrapper
{
	private static final String HTTP_POST = "POST";

	private final BufferingInputStream m_bis;

	private ServletInputStream m_sis;
	private BufferedReader m_br;
	private Map<String, String[]> m_parameters;

	public BufferedRequestWrapper(final HttpServletRequest req, final Integer maxBytesToBuffer) throws IOException
	{
		super(req);
		m_bis = new BufferingInputStream(super.getInputStream(), maxBytesToBuffer);
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		if (m_br == null)
		{
			if (m_sis != null)
			{
				throw new IllegalStateException("getInputStream() has already been called for this request");
			}
			final Charset charset = EncodingHelper.getCharsetByAlias(getCharacterEncoding());
			m_br = new BufferedReader(new InputStreamReader(m_bis, charset));
		}
		return m_br;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		if (m_sis == null)
		{
			if (m_br != null)
			{
				throw new IllegalStateException("getReader() has already been called for this request");
			}
			m_sis = new ServletInputStream()
			{
				@Override
				public int read() throws IOException
				{
					return m_bis.read();
				}
			};
		}
		return m_sis;
	}

	public byte[] getRequestBody()
	{
		byte[] result = m_bis.getBuffer();
		result = result.length > 0 ? result : null;
		return result;
	}

	@Override
	public Map getParameterMap()
	{
		if (parseParameters())
		{
			return m_parameters;
		}
		else
		{
			return super.getParameterMap();
		}
	}

	@Override
	public Enumeration getParameterNames()
	{
		if (parseParameters())
		{

			return Collections.enumeration(m_parameters.keySet());
		}
		else
		{
			return super.getParameterNames();
		}
	}

	@Override
	public String getParameter(final String name)
	{
		if (parseParameters())
		{
			final String[] values = getParameterValues(name);
			return values != null && values.length > 0 ? values[0] : null;
		}
		else
		{
			return super.getParameter(name);
		}
	}

	@Override
	public String[] getParameterValues(final String name)
	{
		if (parseParameters())
		{
			return m_parameters.get(name);
		}
		else
		{
			return super.getParameterValues(name);
		}
	}

	/**
	 * Parse parameters from POST body. Experimental.
	 * Alternatively consider disabling payload logging for cases when endpoint works with POST data as request params.
	 * This is used to support rare case when POST is used as a from post
	 *
	 * @return if parsed parameters should be used
	 */
	@SuppressWarnings({"PMD", "unchecked"})
	private boolean parseParameters()
	{
		final boolean useParsed = HTTP_POST.equals(getMethod());
		if (useParsed && m_parameters == null)
		{
			final Map<String, List<String>> parameters = new HashMap<String, List<String>>();
			final byte[] bodyBytes = getRequestBody();
			if (bodyBytes != null)
			{
				final Charset charset = EncodingHelper.getCharsetByAlias(getCharacterEncoding());
				final String body = EncodingHelper.toCharsetString(bodyBytes, charset);
				final String[] kvItemList = body.split("&");
				for (final String kvItem : kvItemList)
				{
					if (kvItem != null)
					{
						final String[] kv = kvItem.split("=");
						final String k = kv.length > 0 ? EncodingHelper.safeURLDecode(kv[0], charset) : "";
						final String v = kv.length > 1 ? EncodingHelper.safeURLDecode(kv[1], charset) : "";
						if (!"".equals(k))
						{
							List<String> values = parameters.get(k);
							if (values == null)
							{
								values = new ArrayList<String>();
								parameters.put(k, values);
							}
							values.add(v);
						}
					}
				}
			}
			//fill in param cache & merge with super.params()
			m_parameters = new HashMap<String, String[]>();
			final Map<String, String[]> superParameters = super.getParameterMap();
			for (final Map.Entry<String, List<String>> kv : parameters.entrySet())
			{
				final String key = kv.getKey();
				final List<String> values = kv.getValue();
				final String[] superValues = superParameters.get(key);
				if (superValues != null)
				{
					values.addAll(Arrays.asList(superValues));
				}
				m_parameters.put(key, values.toArray(new String[values.size()]));
			}
		}
		return useParsed;
	}
}