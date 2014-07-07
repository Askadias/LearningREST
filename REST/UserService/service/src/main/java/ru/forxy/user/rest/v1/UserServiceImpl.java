package ru.forxy.user.rest.v1;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.user.logic.IUserServiceFacade;
import ru.forxy.user.rest.v1.pojo.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class UserServiceImpl extends AbstractService implements IUserService {

    private IUserServiceFacade userServiceFacade;

    @Override
    public Response getUsers(UriInfo uriInfo, HttpHeaders headers) {
        return respondWith(userServiceFacade.getAllUsers(), uriInfo, headers).build();
    }

    //@Override
    public Response getUsers(final Integer page, final UriInfo uriInfo, final HttpHeaders headers) {
        final EntityPage<User> userPage = userServiceFacade.getUsers(page);
        return respondWith(userPage, uriInfo, headers).build();
    }

    @Override
    public Response getUsers(final Integer page, final Integer size, final UriInfo uriInfo, final HttpHeaders headers) {
        final EntityPage<User> userPage = userServiceFacade.getUsers(page, size);
        return respondWith(userPage, uriInfo, headers).build();
    }

    @Override
    public Response getUser(String email, final UriInfo uriInfo, final HttpHeaders headers) {
        return respondWith(userServiceFacade.getUser(email), uriInfo, headers).build();
    }

    @Override
    public Response createUser(final User user, final UriInfo uriInfo, final HttpHeaders headers) {
        userServiceFacade.createUser(user);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "?email=" + user.getEmail())).build();
    }

    @Override
    public Response updateUser(final User user, final UriInfo uriInfo, final HttpHeaders headers) {
        userServiceFacade.updateUser(user);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "?email=" + user.getEmail())).build();
    }

    @Override
    public Response deleteUser(final String email, final UriInfo uriInfo, final HttpHeaders headers) {
        userServiceFacade.deleteUser(email);
        return Response.ok(new StatusEntity("200",
                "User with email='" + email + "' has been successfully removed")).build();
    }

    public void setUserServiceFacade(final IUserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }
}
