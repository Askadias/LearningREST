package ru.forxy.user.rest.v1;

import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.user.logic.IUserServiceFacade;
import ru.forxy.user.rest.v1.pojo.Credentials;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Provides server-side authentication procedure
 */
public class AuthServiceImpl extends AbstractService implements IAuthService {

    private IUserServiceFacade userServiceFacade;

    @Override
    public Response login(final Credentials credentials, final UriInfo uriInfo, final HttpHeaders headers) {
        return respondWith(userServiceFacade.login(credentials), uriInfo, headers).build();
    }

    @Override
    public Response register(final Credentials credentials, final UriInfo uriInfo, final HttpHeaders headers) {
        userServiceFacade.register(credentials);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + credentials.getEmail())).build();
    }

    public void setUserServiceFacade(final IUserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }
}
