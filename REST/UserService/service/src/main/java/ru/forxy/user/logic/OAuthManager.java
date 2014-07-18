package ru.forxy.user.logic;

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

import java.util.List;

/**
 * OAuth data provider implementation specific to current area
 */
public class OAuthManager implements OAuthDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthManager.class);

    private IUserServiceFacade userServiceFacade;

    private IClientServiceFacade clientServiceFacade;

    @Override
    public Client getClient(String clientId) throws OAuthServiceException {
        LOGGER.info("getClient");
        ru.forxy.oauth.pojo.Client client = clientServiceFacade.getClient(clientId);
        if (client != null) {
            return new Client(client.getClientID(), client.getClientSecret(), true);
        } else {
            return null;
        }
    }

    @Override
    public ServerAccessToken createAccessToken(AccessTokenRegistration accessToken) throws OAuthServiceException {
        LOGGER.info("createAccessToken");
        return new BearerAccessToken(accessToken.getClient(), 8600L);
    }

    @Override
    public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
        LOGGER.info("getAccessToken");
        return null;
    }

    @Override
    public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes, UserSubject subject, String grantType) throws OAuthServiceException {
        LOGGER.info("getPreauthorizedToken");
        return null;
    }

    @Override
    public ServerAccessToken refreshAccessToken(Client client, String refreshToken, List<String> requestedScopes) throws OAuthServiceException {
        LOGGER.info("refreshAccessToken");
        return null;
    }

    @Override
    public void removeAccessToken(ServerAccessToken accessToken) throws OAuthServiceException {
        LOGGER.info("removeAccessToken");
    }

    @Override
    public void revokeToken(Client client, String token, String tokenTypeHint) throws OAuthServiceException {
        LOGGER.info("revokeToken");
    }

    @Override
    public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScope) {
        LOGGER.info("convertScopeToPermissions");
        return null;
    }

    public void setUserServiceFacade(IUserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }

    public void setClientServiceFacade(IClientServiceFacade clientServiceFacade) {
        this.clientServiceFacade = clientServiceFacade;
    }
}
