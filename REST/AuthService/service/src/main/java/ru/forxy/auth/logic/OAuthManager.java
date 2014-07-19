package ru.forxy.auth.logic;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.forxy.auth.pojo.Token;
import ru.forxy.auth.pojo.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OAuth data provider implementation specific to current area
 */
public class OAuthManager implements OAuthDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthManager.class);

    private ITokenGrantServiceFacade tokenGrantServiceFacade;

    private IClientServiceFacade clientServiceFacade;

    @Override
    public Client getClient(final String clientId) throws OAuthServiceException {
        LOGGER.info("getClient");
        ru.forxy.auth.pojo.Client client = clientServiceFacade.getClient(clientId);
        if (client != null) {
            return new Client(client.getClientID(), client.getClientSecret(), true);
        } else {
            return null;
        }
    }

    @Override
    public ServerAccessToken createAccessToken(final AccessTokenRegistration tokenRegistration)
            throws OAuthServiceException {
        LOGGER.info("createAccessToken");
        ServerAccessToken accessToken = new BearerAccessToken(tokenRegistration.getClient(), 8600L);
        tokenGrantServiceFacade.createToken(toToken(accessToken));
        return accessToken;
    }

    private Token toToken(ServerAccessToken accessToken) {
        Token token = new Token();
        token.setClientID(accessToken.getClient().getClientId());
        token.setSubject(toUser(accessToken.getSubject()));
        token.setExpiresIn(accessToken.getExpiresIn());
        token.setIssuedAt(new Date());
        token.setScopes(toScopesList(accessToken.getScopes()));
        return token;
    }

    @Override
    public ServerAccessToken getAccessToken(final String accessTokenKey) throws OAuthServiceException {
        LOGGER.info("getAccessToken");
        Token token = tokenGrantServiceFacade.getToken(accessTokenKey);
        if (token != null) {
            ru.forxy.auth.pojo.Client client = clientServiceFacade.getClient(token.getClientID());
            return new BearerAccessToken(fromClient(client), 8600L);
        } else {
            return null;
        }
    }

    private Client fromClient(ru.forxy.auth.pojo.Client clientTO) {
        if (clientTO != null) {
            Client client = new Client(clientTO.getClientID(), clientTO.getClientSecret(), true,
                    clientTO.getApplicationName(), clientTO.getApplicationWebUri());
            client.setAllowedGrantTypes(clientTO.getAllowedGrantTypes());
            client.setApplicationDescription(clientTO.getApplicationDescription());
            client.setRedirectUris(clientTO.getRedirectUris());
            client.setProperties(clientTO.getProperties());
            client.setSubject(fromUser(clientTO.getSubject()));
            client.setRegisteredAudiences(clientTO.getRegisteredAudiences());
            return client;
        } else {
            return null;
        }
    }

    @Override
    public ServerAccessToken getPreauthorizedToken(final Client client,
                                                   final List<String> requestedScopes,
                                                   final UserSubject subject,
                                                   final String grantType) throws OAuthServiceException {
        LOGGER.info("getPreauthorizedToken");
        return null;
    }

    @Override
    public ServerAccessToken refreshAccessToken(final Client client,
                                                final String refreshToken,
                                                final List<String> requestedScopes) throws OAuthServiceException {
        LOGGER.info("refreshAccessToken");
        return null;
    }

    @Override
    public void removeAccessToken(final ServerAccessToken accessToken) throws OAuthServiceException {
        LOGGER.info("removeAccessToken");
        tokenGrantServiceFacade.deleteToken(accessToken.getTokenKey());
    }

    @Override
    public void revokeToken(final Client client, final String token, final String tokenTypeHint)
            throws OAuthServiceException {
        LOGGER.info("revokeToken");
    }

    @Override
    public List<OAuthPermission> convertScopeToPermissions(final Client client, final List<String> requestedScope) {
        LOGGER.info("convertScopeToPermissions");
        return null;
    }

    public void setTokenGrantServiceFacade(final ITokenGrantServiceFacade tokenGrantServiceFacade) {
        this.tokenGrantServiceFacade = tokenGrantServiceFacade;
    }

    public void setClientServiceFacade(final IClientServiceFacade clientServiceFacade) {
        this.clientServiceFacade = clientServiceFacade;
    }

    private User toUser(final UserSubject userSubject) {
        return userSubject == null ? null :
                new User(userSubject.getId(), userSubject.getRoles(), userSubject.getProperties());
    }

    private UserSubject fromUser(final User user) {
        return user == null ? null : new UserSubject(user.getUserID(), user.getUserID(), user.getRoles());
    }

    private List<String> toScopesList(List<OAuthPermission> permissions) {
        if (permissions != null) {
            List<String> scopes = new ArrayList<>(permissions.size());
            for (OAuthPermission permission : permissions) {
                if (permission.getUris() != null && permission.getUris().size() > 0) {
                    scopes.add(permission.getUris().get(0));
                }
            }
            return scopes;
        } else {
            return null;
        }
    }
}
