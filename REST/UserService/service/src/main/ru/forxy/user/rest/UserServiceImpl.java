package ru.forxy.user.rest;

import net.sf.oval.exception.ValidationFailedException;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.exceptions.ValidationException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.common.support.Constants;
import ru.forxy.user.exceptions.UserServiceExceptions;
import ru.forxy.user.logic.IUserServiceFacade;
import ru.forxy.user.rest.pojo.User;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.regex.Pattern;

public class UserServiceImpl extends AbstractService implements IUserService {

    IUserServiceFacade userServiceFacade;
    private static Pattern emailRegex = Pattern.compile(Constants.EMAIL_REGEX);

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
        validateEmail(requestedUser.getEmail());
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
        validateEmail(email);
        userServiceFacade.deleteUser(email);
        return Response.ok().build();
    }

    private static void validateEmail(String email) {
        if (email == null || "".equals(email)) {
            throw ValidationException.build(new ValidationFailedException("email cannot be null or empty"));
        } else if (!emailRegex.matcher(email).matches()) {
            throw ValidationException.build(new ValidationFailedException("email '" + email + "' is not valid"));
        }
    }

    public void setUserServiceFacade(IUserServiceFacade userServiceFacade) {
        this.userServiceFacade = userServiceFacade;
    }
}
