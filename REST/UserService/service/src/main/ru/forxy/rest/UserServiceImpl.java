package ru.forxy.rest;

import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.exceptions.UserServiceExceptions;
import ru.forxy.logic.IUserServiceFacade;
import ru.forxy.user.rest.IUserService;
import ru.forxy.user.rest.pojo.User;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;

public class UserServiceImpl extends AbstractService implements IUserService {

    IUserServiceFacade userServiceFacade;

    @Override
    public Response getUsers(Integer page, final UriInfo uriInfo, final HttpHeaders headers) {
        EntityPage<User> userPage = userServiceFacade.getUsers(page);
        return respondWith(userPage, uriInfo, headers).build();
    }

    @Override
    public Response getUsers(Integer page, Integer size, final UriInfo uriInfo, final HttpHeaders headers) {
        EntityPage<User> userPage = userServiceFacade.getUsers(page, size);
        return respondWith(userPage, uriInfo, headers).build();
    }

    @Override
    public Response getUser(User requestedUser, final UriInfo uriInfo, final HttpHeaders headers) {
        User user = userServiceFacade.getUser(requestedUser);
        if (user == null) {
            throw new ServiceException(UserServiceExceptions.UserNotFound.getStatusTemplate(), requestedUser.getEmail());
        }
        return respondWith(user, uriInfo, headers).build();
    }

    @Override
    public Response login(User loginUser, final UriInfo uriInfo, final HttpHeaders headers) {
        User user = userServiceFacade.getUser(loginUser);
        if (user != null && Arrays.equals(loginUser.getPassword(), user.getPassword())) {
            return respondWith(user, uriInfo, headers).build();
        } else {
            throw new NotAuthorizedException(loginUser);
        }
    }

    @Override
    public Response updateUser(User user, final UriInfo uriInfo, final HttpHeaders headers) {
        return Response.ok(userServiceFacade.updateUser(user)).build();
    }

    @Override
    public Response createUser(User user, final UriInfo uriInfo, final HttpHeaders headers) {
        return Response.ok(userServiceFacade.createUser(user)).build();
    }

    @Override
    public Response deleteUser(String email, final UriInfo uriInfo, final HttpHeaders headers) {
        userServiceFacade.deleteUser(email);
        return Response.ok().build();
    }

    public void setUserServiceFacade(IUserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }
}
