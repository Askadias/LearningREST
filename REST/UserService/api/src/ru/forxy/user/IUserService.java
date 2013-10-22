package ru.forxy.user;

import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IUserService {

    @GET
    @Path("/users")
    UserServiceResponse getUsers() throws ServiceException;

    @GET
    @Path("/users/{page}/")
    UserServiceResponse getUsers(@PathParam("page") Integer page) throws ServiceException;

    @GET
    @Path("/users/{page}/{size}/")
    UserServiceResponse getUsers(@PathParam("page") Integer page, @PathParam("size") Integer size) throws ServiceException;

    @GET
    @Path("/user")
    UserServiceResponse getUser(User login) throws ServiceException;

    @POST
    @Path("/user/login")
    UserServiceResponse login(User login) throws ServiceException;

    @POST
    @Path("/user/update")
    UserServiceResponse updateUser(User user) throws ServiceException;

    @POST
    @Path("/user/create")
    UserServiceResponse createUser(User user) throws ServiceException;

    @DELETE
    @Path("/user/{email}/")
    UserServiceResponse deleteUser(@PathParam("email") String email) throws ServiceException;
}
