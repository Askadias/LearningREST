package ru.forxy.user.rest.v1;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import ru.forxy.user.logic.IUserServiceFacade;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.List;

/**
 * OAuth data provider implementation specific to current area
 */
public class OAuthManager implements OAuthDataProvider {

    private IUserServiceFacade userServiceFacade;

    @Override
    public Client getClient(String clientId) throws OAuthServiceException {
        User user = userServiceFacade.getUser(clientId);
        if (user != null) {
            return new Client(user.getEmail(), user.getPassword(), true);
        } else {
            return null;
        }
    }

    @Override
    public ServerAccessToken createAccessToken(AccessTokenRegistration accessToken) throws OAuthServiceException {
        return new BearerAccessToken();
    }

    @Override
    public ServerAccessToken getAccessToken(String accessToken) throws OAuthServiceException {
        return null;
    }

    @Override
    public ServerAccessToken getPreauthorizedToken(Client client, List<String> requestedScopes, UserSubject subject, String grantType) throws OAuthServiceException {
        return null;
    }

    @Override
    public ServerAccessToken refreshAccessToken(Client client, String refreshToken, List<String> requestedScopes) throws OAuthServiceException {
        return null;
    }

    @Override
    public void removeAccessToken(ServerAccessToken accessToken) throws OAuthServiceException {

    }

    @Override
    public void revokeToken(Client client, String token, String tokenTypeHint) throws OAuthServiceException {

    }

    @Override
    public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScope) {
        return null;
    }

    public void setUserServiceFacade(IUserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }
}
