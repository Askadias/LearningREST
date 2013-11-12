package ru.forxy.common.logging.wrapper;

import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {
    private final BufferingOutputStream m_bos;

    private PrintWriter m_pw;
    private ServletOutputStream m_sos;

    private int m_responseStatus = HttpServletResponse.SC_OK;
    private String m_responseRedirectURL;
    private final Map<String, List<String>> m_responseHeaders = new LinkedHashMap<String, List<String>>();

    public BufferedResponseWrapper(final HttpServletResponse response, final Integer maxBytesToBuffer) {
        super(response);
        m_bos = new BufferingOutputStream(maxBytesToBuffer);
    }

    @Override
    public void setStatus(final int sc) {
        m_responseStatus = sc;
        super.setStatus(sc);
    }

    @Override
    public void setStatus(final int sc, final String sm) {
        m_responseStatus = sc;
        super.setStatus(sc, sm);
    }

    @Override
    public void sendError(final int sc) throws IOException {
        m_responseStatus = sc;
        super.sendError(sc);
    }

    @Override
    public void sendError(final int sc, final String msg) throws IOException {
        m_responseStatus = sc;
        super.sendError(sc, msg);
    }

    @Override
    public void sendRedirect(final String location) throws IOException {
        m_responseRedirectURL = location;
        m_responseStatus = HttpServletResponse.SC_MOVED_TEMPORARILY;
        super.sendRedirect(location);
    }

    @Override
    public void setHeader(final String name, final String value) {
        m_responseHeaders.put(name, Collections.singletonList(value));
        super.setHeader(name, value);
    }

    @Override
    public void addHeader(final String name, final String value) {
        List<String> values = m_responseHeaders.get(name);
        if (values != null) {
            values.add(value);
        } else {
            values = new ArrayList<String>();
            values.add(value);
            m_responseHeaders.put(name, values);
        }
        super.addHeader(name, value);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (m_pw == null) {
            m_pw = new TeePrintWriter(super.getWriter(),
                    new PrintWriter(new OutputStreamWriter(m_bos, getCharacterEncoding())));
        }
        return m_pw;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (m_sos == null) {
            final TeeOutputStream tos = new TeeOutputStream(super.getOutputStream(), m_bos);
            m_sos = new ServletOutputStream() {
                @Override
                public void flush() throws IOException {
                    tos.flush();
                }

                @Override
                public void close() throws IOException {
                    tos.close();
                }

                @Override
                public void write(final int arg) throws IOException {
                    tos.write(arg);
                }
            };
        }
        return m_sos;
    }

    public int getResponseStatus() {
        return m_responseStatus;
    }

    public byte[] getResponseBody() {
        byte[] result = m_bos.getBuffer();
        result = result.length > 0 ? result : null;
        return result;
    }

    public String getResponseRedirectURL() {
        return m_responseRedirectURL;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return m_responseHeaders;
    }
}