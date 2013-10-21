package ru.forxy.user;

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
    UserServiceResponse getUsers();

    @GET
    @Path("/users/{page}/")
    UserServiceResponse getUsers(@PathParam("page") Integer page);

    @GET
    @Path("/users/{page}/{size}/")
    UserServiceResponse getUsers(@PathParam("page") Integer page, @PathParam("size") Integer size);

    @GET
    @Path("/user")
    UserServiceResponse getUser(User login);

    @POST
    @Path("/user/login")
    UserServiceResponse login(User login);

    @POST
    @Path("/user/update")
    UserServiceResponse updateUser(User user);

    @POST
    @Path("/user/create")
    UserServiceResponse createUser(User user);

    @DELETE
    @Path("/user/{email}/")
    UserServiceResponse deleteUser(@PathParam("email") String email);
}
