package ru.forxy.user.rest.v1;

import ru.forxy.user.rest.v1.pojo.Credentials;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/oauth/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IAuthService {

    @POST
    Response login(final Credentials credentials, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @PUT
    Response register(final Credentials credentials, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);
}
