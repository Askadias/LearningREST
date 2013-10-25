package ru.forxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.service.AbstractService;
import ru.forxy.common.service.ErrorResponseBuilder;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.exceptions.UserAlreadyExistException;
import ru.forxy.exceptions.UserNotFoundException;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;

public class UserServiceImpl extends AbstractService implements IUserService {

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private IUserDAO userDAO;

    @Override
    public Response getUsers(Integer page, final UriInfo uriInfo, final HttpHeaders headers) {
        List<User> users = userDAO.findAll(new PageRequest(page, DEFAULT_PAGE_SIZE)).getContent();
        return respondWith(users, uriInfo, headers).build();
    }

    @Override
    public Response getUsers(Integer page, Integer size, final UriInfo uriInfo, final HttpHeaders headers) {
        List<User> users = userDAO.findAll(new PageRequest(page, size)).getContent();
        return respondWith(users, uriInfo, headers).build();
    }

    @Override
    public Response getUser(User queryUser, final UriInfo uriInfo, final HttpHeaders headers) {
        if (queryUser != null && queryUser.getEmail() != null) {
            User user = userDAO.findOne(queryUser.getEmail());
            return respondWith(user, uriInfo, headers).build();
        }
        throw new UserNotFoundException(queryUser);
    }

    @Override
    public Response login(User loginUser, final UriInfo uriInfo, final HttpHeaders headers) {
        User user = userDAO.findOne(loginUser.getEmail());
        if (user != null && Arrays.equals(loginUser.getPassword(), user.getPassword())) {
            return respondWith(user, uriInfo, headers).build();
        } else {
            throw new NotAuthorizedException(loginUser);
        }
    }

    @Override
    public Response updateUser(User user, final UriInfo uriInfo, final HttpHeaders headers) {
        if (user.getEmail() != null && user.getPassword() != null) {
            userDAO.save(user);
            return Response.ok(user).build();
        } else {
            throw new BadRequestException(
                    ErrorResponseBuilder.build(Response.Status.BAD_REQUEST, "User's email or password are empty"));
        }
    }

    @Override
    public Response createUser(User user, final UriInfo uriInfo, final HttpHeaders headers) {
        if (user.getEmail() != null) {
            if (!userDAO.exists(user.getEmail())) {
                userDAO.save(user);
                return Response.ok(user).build();
            } else {
                throw new UserAlreadyExistException(user);
            }
        } else {
            throw new BadRequestException(
                    ErrorResponseBuilder.build(Response.Status.BAD_REQUEST, "User's email is null"));
        }
    }

    @Override
    public Response deleteUser(String email, final UriInfo uriInfo, final HttpHeaders headers) {
        if (userDAO.exists(email)) {
            userDAO.delete(email);
            return Response.ok(email).build();
        } else {
            throw new UserNotFoundException(new User(email, null));
        }
    }
}
