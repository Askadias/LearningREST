package ru.forxy.service;

import ru.forxy.service.pojo.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UserService/")
public interface IUserService {

    @GET
    @Path("/users")
    @Produces("application/json")
    List<User> getUsers();

    @GET
    @Path("/users/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    User getUser(@PathParam("id") Integer id);

    @GET
    @Path("/bad")
    Response getBadResponse();

    @PUT
    @Path("/users")
    @Consumes("application/json")
    void updateUser(User user);

    @POST
    @Path("/users")
    @Consumes("application/json")
    void addUser(User user);

    @DELETE
    @Path("/users/{id}")
    void deleteUser(@PathParam("id") Integer id);
}
