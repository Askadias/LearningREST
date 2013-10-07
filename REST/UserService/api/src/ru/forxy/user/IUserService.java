package ru.forxy.user;

import ru.forxy.user.pojo.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IUserService {

    @GET
    @Path("/users")
    List<User> getUsers();

    @GET
    @Path("/user/{email}.{password}")
    User login(@PathParam("email") String email, @PathParam("password") String password);

    @PUT
    @Path("/user")
    void updateUser(User user);

    @POST
    @Path("/user")
    void addUser(User user);

    @DELETE
    @Path("/user/{email}")
    void deleteUser(@PathParam("email") String email);
}
