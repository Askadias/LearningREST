package ru.forxy.service;

import ru.forxy.pojo.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
public interface IUserService {

    @GET
    @Path("/users")
    @Produces("application/json")
    List<User> getUsers();

    @GET
    @Path("/users/{email}.{password}")
    @Consumes("application/json")
    @Produces("application/json")
    User login(@PathParam("email") String email, @PathParam("password") String password);

    @PUT
    @Path("/users")
    @Consumes("application/json")
    void updateUser(User user);

    @POST
    @Path("/users")
    @Consumes("application/json")
    void addUser(User user);

    @DELETE
    @Path("/users/{email}")
    void deleteUser(@PathParam("email") String email);
}
