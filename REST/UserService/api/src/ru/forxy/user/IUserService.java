package ru.forxy.user;

import ru.forxy.user.pojo.User;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/users/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IUserService {

    @GET
    @Path("/{page}/")
    Response getUsers(@PathParam("page") Integer page,
                      @Context final UriInfo uriInfo,
                      @Context final HttpHeaders headers);

    @GET
    @Path("/{page}/{size}/")
    Response getUsers(@PathParam("page") Integer page,
                      @PathParam("size") Integer size,
                      @Context final UriInfo uriInfo,
                      @Context final HttpHeaders headers);

    @GET
    Response getUser(@QueryParam("") User user,
                     @Context final UriInfo uriInfo,
                     @Context final HttpHeaders headers);

    @POST
    @Path("/login")
    Response login(User login,
                   @Context final UriInfo uriInfo,
                   @Context final HttpHeaders headers);

    @POST
    Response updateUser(User user,
                        @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers);

    @PUT
    Response createUser(User user,
                        @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers);

    @DELETE
    @Path("/{email}")
    Response deleteUser(@PathParam("email") String email,
                        @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers);
}
