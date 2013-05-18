package ru.forxy.service;

import ru.forxy.db.dto.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/UserService/")
@Consumes("application/json")
@Produces("application/json")
public interface IRestService {

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
}
