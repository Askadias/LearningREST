package ru.forxy.user.rest.v1;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpGet;
import ru.forxy.common.exceptions.ClientException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.client.RestServiceClientSupport;
import ru.forxy.common.rest.client.transport.ITransport;
import ru.forxy.common.rest.client.transport.support.ObjectMapperProvider;
import ru.forxy.user.rest.v1.pojo.User;

import javax.ws.rs.core.HttpHeaders;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
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

    public UserServiceClient() {
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
    public List<User> getUsers(String transactionGUID) {
        final String confUrl = endpoint + "users";

        final ITransport.Response<List, StatusEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(List.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityPage<User> getUsers(final String transactionGUID, final Integer page) {
        final String confUrl = endpoint + "users?page=" + page;

        final ITransport.Response<EntityPage, StatusEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(EntityPage.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityPage<User> getUsers(final String transactionGUID, Integer page, Integer size) {
        final String confUrl = endpoint + "users?page=" + page + "&size=" + size;

        final ITransport.Response<EntityPage, StatusEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(EntityPage.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public User getUser(final String transactionGUID, String email) {
        final String confUrl = endpoint + "users/" + email;

        final ITransport.Response<User, StatusEntity> response =
                transport.performGet(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(User.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public StatusEntity createUser(final String transactionGUID, final User user) {
        final String confUrl = endpoint + "users";

        final ITransport.Response<StatusEntity, StatusEntity> response =
                transport.performPut(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        marshal(user), createResponseHandler(StatusEntity.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public StatusEntity updateUser(final String transactionGUID, final User user) {
        final String confUrl = endpoint + "users";

        final ITransport.Response<StatusEntity, StatusEntity> response =
                transport.performPost(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        marshal(user), createResponseHandler(StatusEntity.class));
        return checkForError(response);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public StatusEntity deleteUser(String transactionGUID, String email) {
        final String confUrl = endpoint + "users/" + email;

        final ITransport.Response<StatusEntity, StatusEntity> response =
                transport.performDelete(confUrl, buildHeaders(transactionGUID, endpoint, HttpGet.METHOD_NAME),
                        createResponseHandler(StatusEntity.class));
        return checkForError(response);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
