package ru.forxy.user.rest.v1;

import ru.forxy.user.rest.v1.pojo.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/users/")
@Produces(MediaType.APPLICATION_JSON)
public interface IUserService {

    @GET
    Response getUsers(@Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    /*@GET
    Response getUsers(@MatrixParam("page") final Integer page, @Context final UriInfo uriInfo,
                      @Context final HttpHeaders headers);*/

    @GET
    Response getUsers(@MatrixParam("page") final Integer page, @MatrixParam("size") final Integer size,
                      @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @GET
    @Path("/{email}/")
    Response getUser(@PathParam("email") final String email, @Context final UriInfo uriInfo,
                     @Context final HttpHeaders headers);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(final User user, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUser(final User user, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @DELETE
    @Path("/{email}/")
    Response deleteUser(@PathParam("email") final String email, @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers);
}
