package ru.forxy.user.rest.v1;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import ru.forxy.common.exceptions.ClientException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.ErrorEntity;
import ru.forxy.common.rest.client.RestServiceClientSupport;
import ru.forxy.common.rest.client.transport.ITransport;
import ru.forxy.common.rest.client.transport.support.ObjectMapperProvider;
import ru.forxy.user.rest.v1.pojo.User;

import javax.ws.rs.core.HttpHeaders;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User service client implementation
 */
public class UserServiceClient extends RestServiceClientSupport implements IUserServiceClient {
    protected static final String CLIENT_ID = "Client-ID";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final String TRANSACTION_GUID_PARAM = "transactionGUID";

    private String endpoint;

    private String clientId;

    public UserServiceClient()
    {
    }

    public UserServiceClient(final String endpoint, final String clientId, final ITransport transport) {
        this.endpoint = endpoint;
        this.clientId = clientId;
        this.transport = transport;

        // @formatter:off
        mapper = ObjectMapperProvider.getMapper(
                ObjectMapperProvider.Config.newInstance().withoutPropertyNamingStrategy().
                        withDateFormat(SIMPLE_DATE_FORMAT).
                        writeEmptyArrays(true).writeNullMapValues(true)
        );
        // @formatter:on

    }

    private Map<String, String> buildHeaders(final String transactionGUID, final String url, final String method)
            throws ClientException {
        validateGUID(TRANSACTION_GUID_PARAM, transactionGUID, true);

        final String txGUID = StringUtils.defaultIfEmpty(transactionGUID, UUID.randomUUID().toString());
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.ACCEPT, JSON_CONTENT_TYPE);
        headers.put(HttpHeaders.CONTENT_TYPE, JSON_CONTENT_TYPE);
        headers.put(TRANSACTION_GUID, txGUID);
        headers.put(CLIENT_ID, clientId);
        return headers;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityPage<User> getUsers(final String transactionGUID, final Integer page) {
        final String confUrl = endpoint + "users/" + page;

        final ITransport.Response<EntityPage, ErrorEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(EntityPage.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityPage<User> getUsers(final String transactionGUID, Integer page, Integer size) {
        final String confUrl = endpoint + "users/" + page + "/" + size;

        final ITransport.Response<EntityPage, ErrorEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(EntityPage.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public User getUser(final String transactionGUID, User user) {
        final String confUrl = endpoint + "users?email=" + user.getEmail();

        final ITransport.Response<User, ErrorEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(User.class));
        return checkForError(response);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
