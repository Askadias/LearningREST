package ru.forxy.auth.rest.v1;

import ru.forxy.auth.logic.IUserManager;
import ru.forxy.auth.rest.v1.pojo.Gender;
import ru.forxy.auth.rest.v1.pojo.User;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.AbstractService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/users/")
@Produces(MediaType.APPLICATION_JSON)
public class UserServiceEndpoint extends AbstractService {

    private IUserManager userServiceFacade;

    @GET
    public Response getUsers(@QueryParam("page") final Integer page,
                             @QueryParam("size") final Integer size,
                             @QueryParam("sort_dir") final SortDirection sortDirection,
                             @QueryParam("sorted_by") final String sortedBy,
                             @QueryParam("email") final String emailFilter,
                             @QueryParam("login") final String loginFilter,
                             @QueryParam("first_name") final String firstNameFilter,
                             @QueryParam("last_name") final String lastNameFilter,
                             @QueryParam("gender") final Gender genderFilter,
                             @Context final UriInfo uriInfo,
                             @Context final HttpHeaders headers) {
        return respondWith(page == null && size == null ?
                        userServiceFacade.getAllUsers() :
                        userServiceFacade.getUsers(page, size, sortDirection, sortedBy,
                                new User(emailFilter, loginFilter, firstNameFilter, lastNameFilter, genderFilter)),
                uriInfo, headers).build();
    }

    @GET
    @Path("/{email}/")
    public Response getUser(@PathParam("email") final String email,
                            @Context final UriInfo uriInfo,
                            @Context final HttpHeaders headers) {
        return respondWith(userServiceFacade.getUser(email), uriInfo, headers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(final User user,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        userServiceFacade.createUser(user);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + user.getEmail())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(final User user,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        userServiceFacade.updateUser(user);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + user.getEmail())).build();
    }

    @DELETE
    @Path("/{email}/")
    public Response deleteUser(@PathParam("email") final String email,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        userServiceFacade.deleteUser(email);
        return Response.ok(new StatusEntity("200",
                "User with email='" + email + "' has been successfully removed")).build();
    }

    public void setUserManager(final IUserManager userManager) {
        this.userServiceFacade = userManager;
    }
}
