package ru.forxy.service;

import ru.forxy.service.pojo.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UserService/")
@Consumes("application/json")
@Produces("application/json")
public interface IUserService {

    @GET
    @Path("/users")
    List<User> getUsers();

    @GET
    @Path("/users/{id}")
    User getUser(@PathParam("id") Integer id);

    @GET
    @Path("/bad")
    Response getBadResponse();

    @PUT
    @Path("/users")
    void updateUser(User user);

    @POST
    @Path("/users")
    void addUser(User user);

    @DELETE
    @Path("/users/{id}")
    Response deleteUser(@PathParam("id") Integer id);
}
