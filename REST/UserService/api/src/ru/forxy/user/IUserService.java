package ru.forxy.user;

import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IUserService {

    @GET
    @Path("/users")
    List<User> getUsers() throws ServiceException;

    @GET
    @Path("/users/{page}/")
    List<User> getUsers(@PathParam("page") Integer page) throws ServiceException;

    @GET
    @Path("/users/{page}/{size}/")
    List<User> getUsers(@PathParam("page") Integer page, @PathParam("size") Integer size) throws ServiceException;

    @GET
    @Path("/user")
    User getUser(User login) throws ServiceException;

    @POST
    @Path("/user/login")
    User login(User login) throws ServiceException;

    @POST
    @Path("/user/update")
    Response updateUser(User user) throws ServiceException;

    @POST
    @Path("/user/create")
    Response createUser(User user) throws ServiceException;

    @DELETE
    @Path("/user/{email}/")
    Response deleteUser(@PathParam("email") String email) throws ServiceException;
}
