package ru.forxy.auth.rest.v1

import org.apache.commons.io.IOUtils
import org.apache.commons.lang.StringUtils
import org.apache.http.client.methods.HttpPost
import ru.forxy.common.exceptions.ClientException
import ru.forxy.common.exceptions.HttpEventLogId
import ru.forxy.common.pojo.StatusEntity
import ru.forxy.common.rest.client.RestServiceClientSupport
import ru.forxy.common.rest.client.transport.HttpClientSSLKeyStore
import ru.forxy.common.rest.client.transport.ITransport
import ru.forxy.common.rest.client.transport.support.ObjectMapperProvider
import ru.forxy.fraud.rest.v1.velocity.VelocityMetric
import ru.forxy.fraud.rest.v1.velocity.redis.VMetric

import javax.ws.rs.core.HttpHeaders
import java.text.SimpleDateFormat

/**
 * Client service client implementation
 */
class VelocityServiceClient extends RestServiceClientSupport implements IVelocityServiceClient {
    protected static final String CLIENT_ID = 'Client-ID';
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat('yyyy-MM-dd\'T\'HH:mm:ssZ');
    private static final String TRANSACTION_GUID_PARAM = 'transactionGUID';
    private static final HttpClientSSLKeyStore TRUST_STORE;

    private String endpoint;

    private String clientId;

    static {
        try {
            final InputStream trustStoreStream = VelocityServiceClient.class.getResourceAsStream('/cert/oauthTrustStore.jks');
            final byte[] trustStoreBytes = IOUtils.toByteArray(trustStoreStream);
            TRUST_STORE = new HttpClientSSLKeyStore(new ByteArrayInputStream(trustStoreBytes), '5ecret0AUTHPa55word');
        } catch (Exception e) {
            throw new ClientException(new StatusEntity('400', e), e, HttpEventLogId.InvalidClientInput);
        }
    }

    VelocityServiceClient() {
    }

    VelocityServiceClient(final String endpoint, final String clientId, final ITransport transport) {
        this.endpoint = endpoint;
        this.clientId = clientId;
        this.transport = transport;

        // @formatter:off
        mapper = ObjectMapperProvider.getDefaultMapper();/*ObjectMapperProvider.getMapper(
                ObjectMapperProvider.Config.newInstance().
                        withDateFormat(SIMPLE_DATE_FORMAT).
                        writeEmptyArrays(true).writeNullMapValues(true)
        );*/
        // @formatter:on

    }

    private Map<String, String> buildHeaders(final String transactionGUID, final String url, final String method)
            throws ClientException {
        validateGUID(TRANSACTION_GUID_PARAM, transactionGUID, true);

        final String txGUID = StringUtils.defaultIfEmpty(transactionGUID, UUID.randomUUID().toString());
        final Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, JSON_CONTENT_TYPE);
        headers.put(HttpHeaders.CONTENT_TYPE, JSON_CONTENT_TYPE);
        headers.put(TRANSACTION_GUID, txGUID);
        headers.put(CLIENT_ID, clientId);
        return headers;
    }

    @Override
    @SuppressWarnings(['rawtypes', 'unchecked'])
    List<VelocityMetric> check(final String transactionGUID, final Map<String, String> metrics) {
        final String confUrl = endpoint + 'velocity/';

        final ITransport.Response<List, StatusEntity> response =
                transport.performPost(confUrl, buildHeaders(transactionGUID, endpoint, HttpPost.METHOD_NAME),
                        marshal(metrics), createResponseHandler(List.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings(['rawtypes', 'unchecked'])
    List<VMetric> rcheck(final String transactionGUID, final Map<String, String> metrics) {
        final String confUrl = endpoint + 'velocity/rcheck/';

        final ITransport.Response<List, StatusEntity> response =
                transport.performPost(confUrl, buildHeaders(transactionGUID, endpoint, HttpPost.METHOD_NAME),
                        marshal(metrics), createResponseHandler(List.class));
        return checkForError(response);
    }

    void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
