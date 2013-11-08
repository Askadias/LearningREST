package ru.forxy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.service.AbstractService;
import ru.forxy.db.dao.IUserDAO;
import ru.forxy.exceptions.UserServiceExceptions;
import ru.forxy.user.IUserService;
import ru.forxy.user.pojo.User;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;

public class UserServiceImpl extends AbstractService implements IUserService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IUserDAO userDAO;

    @Override
    public Response getUsers(Integer page, final UriInfo uriInfo, final HttpHeaders headers) {
        Page<User> p = userDAO.findAll(new PageRequest(page, DEFAULT_PAGE_SIZE));
        EntityPage<User> userPage = new EntityPage<User>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
        return respondWith(userPage, uriInfo, headers).build();
    }

    @Override
    public Response getUsers(Integer page, Integer size, final UriInfo uriInfo, final HttpHeaders headers) {
        Page<User> p = userDAO.findAll(new PageRequest(page, size));
        EntityPage<User> userPage = new EntityPage<User>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
        return respondWith(userPage, uriInfo, headers).build();
    }

    @Override
    public Response getUser(User queryUser, final UriInfo uriInfo, final HttpHeaders headers) {
        if (queryUser != null && queryUser.getEmail() != null) {
            User user = userDAO.findOne(queryUser.getEmail());
            if (user == null) {
                throw new ServiceException(UserServiceExceptions.UserNotFound.getStatusTemplate(), queryUser.getEmail());
            }
            return respondWith(user, uriInfo, headers).build();
        }
        throw new ServiceException(UserServiceExceptions.EmailIsNullOrEmpty.getStatusTemplate());
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
            throw new ServiceException(UserServiceExceptions.EmptyLoginEmailOrPassword.getStatusTemplate());
        }
    }

    @Override
    public Response createUser(User user, final UriInfo uriInfo, final HttpHeaders headers) {
        if (user.getEmail() != null) {
            if (!userDAO.exists(user.getEmail())) {
                userDAO.save(user);
                return Response.ok(user).build();
            } else {
                throw new ServiceException(UserServiceExceptions.UserAlreadyExists.getStatusTemplate(), user.getEmail());
            }
        } else {
            throw new ServiceException(UserServiceExceptions.EmailIsNullOrEmpty.getStatusTemplate());
        }
    }

    @Override
    public Response deleteUser(String email, final UriInfo uriInfo, final HttpHeaders headers) {
        if (userDAO.exists(email)) {
            userDAO.delete(email);
            return Response.ok(email).build();
        } else {
            throw new ServiceException(UserServiceExceptions.UserNotFound.getStatusTemplate(), email);
        }
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
