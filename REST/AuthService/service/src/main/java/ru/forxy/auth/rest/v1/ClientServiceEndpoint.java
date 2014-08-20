package ru.forxy.auth.rest.v1;

import ru.forxy.auth.logic.IClientManager;
import ru.forxy.auth.rest.v1.pojo.Client;
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

@Path("/clients/")
@Produces(MediaType.APPLICATION_JSON)
public class ClientServiceEndpoint extends AbstractService {

    private IClientManager clientServiceFacade;

    @GET
    public Response getClients(@QueryParam("page") final Integer page,
                               @QueryParam("size") final Integer size,
                               @QueryParam("sort_dir") final SortDirection sortDirection,
                               @QueryParam("sorted_by") final String sortedBy,
                               @QueryParam("client_id") final String idFilter,
                               @QueryParam("name") final String nameFilter,
                               @QueryParam("updated_by") final String updatedByFilter,
                               @QueryParam("created_by") final String createdByFilter,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        return respondWith(page == null && size == null ?
                        clientServiceFacade.getAllClients() :
                        clientServiceFacade.getClients(page, size, sortDirection, sortedBy,
                                new Client(idFilter, nameFilter, updatedByFilter, createdByFilter)),
                uriInfo, headers).build();
    }

    @GET
    @Path("/{client_id}/")
    public Response getClient(@PathParam("client_id") final String clientID,
                              @Context final UriInfo uriInfo,
                              @Context final HttpHeaders headers) {
        return respondWith(clientServiceFacade.getClient(clientID), uriInfo, headers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerClient(final Client client,
                                   @Context final UriInfo uriInfo,
                                   @Context final HttpHeaders headers) {
        clientServiceFacade.createClient(client);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + client.getClientID())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateClient(final Client client,
                                 @Context final UriInfo uriInfo,
                                 @Context final HttpHeaders headers) {
        clientServiceFacade.updateClient(client);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + client.getClientID())).build();
    }

    @DELETE
    @Path("/{client_id}/")
    public Response deleteClient(@PathParam("client_id") final String clientID,
                                 @Context final UriInfo uriInfo,
                                 @Context final HttpHeaders headers) {
        clientServiceFacade.deleteClient(clientID);
        return Response.ok(new StatusEntity("200",
                "Client with ID='" + clientID + "' has been successfully removed")).build();
    }

    public void setClientServiceFacade(final IClientManager clientServiceFacade) {
        this.clientServiceFacade = clientServiceFacade;
    }
}
