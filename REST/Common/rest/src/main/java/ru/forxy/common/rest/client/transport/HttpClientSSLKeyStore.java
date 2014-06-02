/**
 * Copyright 2012 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ru.forxy.common.rest.client.transport;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;

/**
 * Facade class providing an access to <tt>keystore</tt> and <tt>truststore</tt> certificates,
 * to ease 1-way or 2-way SSL configuration for the HTTP client.
 * @author v-dchabrovsky
 */
public final class HttpClientSSLKeyStore
{
    private final KeyStore m_keyStore;
    private final String m_keyStorePassword;
    private final KeyStore m_trustStore;
    private final boolean m_disableHostnameVerifier;

    /**
     * Constructs certificates store for 1-way SSL communication, when only <tt>truststore</tt> is used
     * to authenticate the server during SSL handshake.
     * @param trustStoreStream stream to read <tt>truststore</tt> file from
     * @param trustStorePassword <tt>truststore</tt> password
     * @throws Exception when there is a problem reading the <tt>truststore</tt> file from the provided stream
     */
    public HttpClientSSLKeyStore(final InputStream trustStoreStream, final String trustStorePassword)
            throws Exception
    {
        this(trustStoreStream, trustStorePassword, false);
    }

    /**
     * Constructs certificates store for 1-way SSL communication, when only <tt>truststore</tt> is used
     * to authenticate the server during SSL handshake.
     * @param trustStoreStream stream to read <tt>truststore</tt> file from
     * @param trustStorePassword <tt>truststore</tt> password
     * @param disableHostnameVerifier specifies whether the hostname verifier should be disabled in the
     * {@link org.apache.http.conn.ssl.SSLSocketFactory} returned by the {@link #getSocketFactory()} method
     * @throws Exception when there is a problem reading the <tt>truststore</tt> file from the provided stream
     */
    public HttpClientSSLKeyStore(final InputStream trustStoreStream, final String trustStorePassword,
            final boolean disableHostnameVerifier) throws Exception
    {
        this(null, null, trustStoreStream, trustStorePassword, disableHostnameVerifier);
    }

    /**
     * Constructs certificates store for 2-way SSL communication, when, in addition to authenticating the server, client
     * is requested by server (during SSL handshake) to provide the cerificate.
     * @param keyStoreStream stream to read <tt>keystore</tt> file from
     * @param keyStorePassword <tt>keystore</tt> password
     * @param trustStoreStream stream to read <tt>truststore</tt> file from
     * @param trustStorePassword <tt>truststore</tt> password
     * @throws Exception when there is a problem reading either <tt>keystore</tt> or
     * <tt>truststore</tt> file from the provided stream
     */
    public HttpClientSSLKeyStore(final InputStream keyStoreStream, final String keyStorePassword,
            final InputStream trustStoreStream, final String trustStorePassword) throws Exception
    {
        this(keyStoreStream, keyStorePassword, trustStoreStream, trustStorePassword, false);
    }

    /**
     * Constructs certificates store for 2-way SSL communication, when, in addition to authenticating the server, client
     * is requested by server (during SSL handshake) to provide the cerificate.
     * @param keyStoreStream stream to read <tt>keystore</tt> file from
     * @param keyStorePassword <tt>keystore</tt> password
     * @param trustStoreStream stream to read <tt>truststore</tt> file from
     * @param trustStorePassword <tt>truststore</tt> password
     * @param disableHostnameVerifier specifies whether the hostname verifier should be disabled in the
     * {@link org.apache.http.conn.ssl.SSLSocketFactory} returned by the {@link #getSocketFactory()} method
     * @throws Exception when there is a problem reading either <tt>keystore</tt> or
     * <tt>truststore</tt> file from the provided stream
     */
    public HttpClientSSLKeyStore(final InputStream keyStoreStream, final String keyStorePassword,
            final InputStream trustStoreStream, final String trustStorePassword,
            final boolean disableHostnameVerifier) throws Exception
    {
        if (keyStoreStream != null && !StringUtils.isEmpty(keyStorePassword))
        {
            m_keyStore = loadKeyStore(keyStoreStream, keyStorePassword);
            m_keyStorePassword = keyStorePassword;
        }
        else
        {
            m_keyStore = null;
            m_keyStorePassword = null;
        }
        m_trustStore = loadKeyStore(trustStoreStream, trustStorePassword);
        m_disableHostnameVerifier = disableHostnameVerifier;
    }

    private KeyStore loadKeyStore(final InputStream input, final String password) throws Exception
    {
        final KeyStore keystore = KeyStore.getInstance("jks");
        keystore.load(input, password.toCharArray());
        return keystore;
    }

    /**
     * Constructs the {@link org.apache.http.conn.ssl.SSLSocketFactory} according to the options specified during the
     * construction time. The returned instance can be used to register an <b><tt>https</tt></b>
     * sheme in the {@link org.apache.http.conn.scheme.SchemeRegistry} while costructing an Apache HTTP client.
     * @return <tt>SSLSocketFactory</tt> instance
     */
    public SSLSocketFactory getSocketFactory()
    {
        // @formatter:off
        final X509HostnameVerifier hostnameVerifier = m_disableHostnameVerifier
            ? SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
            : SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        // @formatter:on
        try
        {
            return new SSLSocketFactory(SSLSocketFactory.TLS, m_keyStore/*can be null*/,
                    m_keyStorePassword/*can be null*/, m_trustStore, null, hostnameVerifier);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Failed to create SSL Socket Factory", e);
        }
    }
}
