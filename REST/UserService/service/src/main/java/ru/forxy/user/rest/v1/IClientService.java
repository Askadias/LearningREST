package ru.forxy.user.rest.v1;

import ru.forxy.common.pojo.SortDirection;
import ru.forxy.oauth.pojo.Client;

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

@Path("/clients/")
@Produces(MediaType.APPLICATION_JSON)
public interface IClientService {

    @GET
    Response getClients(@QueryParam("page") final Integer page,
                      @QueryParam("size") final Integer size,
                      @QueryParam("sortDir") final SortDirection sortDirection,
                      @QueryParam("sortedBy") final String sortedBy,
                      @QueryParam("") final Client filter,
                      @Context final UriInfo uriInfo,
                      @Context final HttpHeaders headers);

    @GET
    @Path("/{clientID}/")
    Response getClient(@PathParam("clientID") final String clientID, @Context final UriInfo uriInfo,
                     @Context final HttpHeaders headers);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response registerClient(final Client client, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateClient(final Client client, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);

    @DELETE
    @Path("/{clientID}/")
    Response deleteClient(@PathParam("clientID") final String clientID, @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers);
}
