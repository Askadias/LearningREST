package ru.forxy.user;

import ru.forxy.user.pojo.User;
import ru.forxy.user.pojo.UserServiceResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IUserService {

    @GET
    @Path("/users")
    UserServiceResponse getUsers();

    @GET
    @Path("/user/{email}/{password}")
    UserServiceResponse login(@PathParam("email") String email, @PathParam("password") byte[] password);

    @PUT
    @Path("/user")
    UserServiceResponse updateUser(User user);

    @POST
    @Path("/user")
    UserServiceResponse createUser(User user);

    @DELETE
    @Path("/user/{email}")
    UserServiceResponse deleteUser(@PathParam("email") String email);

    @DELETE
    @Path("/user")
    UserServiceResponse deleteUser(User user);
}
