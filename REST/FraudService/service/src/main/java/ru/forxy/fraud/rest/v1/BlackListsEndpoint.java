package ru.forxy.fraud.rest.v1;

import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.fraud.logic.blacklist.IBlackListManager;
import ru.forxy.fraud.rest.v1.list.BlackListItem;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

@Path("/blacklists/")
@Produces(MediaType.APPLICATION_JSON)
public class BlackListsEndpoint extends AbstractService {

    private IBlackListManager blackListManager;

    @GET
    public Response getBlackLists(@Context final UriInfo uriInfo, @Context final HttpHeaders headers) {
        return respondWith(blackListManager.getAllBlackLists(), uriInfo, headers).build();
    }

    @GET
    @Path("/{type}/{value}")
    public Response getBlackListItem(@PathParam("type") final String type, @PathParam("value") final String value,
                                     @Context final UriInfo uriInfo,
                                     @Context final HttpHeaders headers) {
        return respondWith(blackListManager.get(type, value), uriInfo, headers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addToBlackList(final BlackListItem blackListItem,
                                   @Context final UriInfo uriInfo,
                                   @Context final HttpHeaders headers) {
        blackListManager.add(blackListItem);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + blackListItem.getKey().getType()
                + "/" + blackListItem.getKey().getValue())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBlackList(final BlackListItem blackListItem,
                                    @Context final UriInfo uriInfo,
                                    @Context final HttpHeaders headers) {
        blackListManager.update(blackListItem);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + blackListItem.getKey().getType()
                + "/" + blackListItem.getKey().getValue())).build();
    }

    @DELETE
    @Path("/{type}/{value}")
    public Response deleteBlackList(@PathParam("type") final String type,
                                    @PathParam("value") final String value,
                                    @Context final UriInfo uriInfo,
                                    @Context final HttpHeaders headers) {
        blackListManager.delete(new BlackListItem());
        return Response.ok(new StatusEntity("200", "BlackList item with type='" + type + "' and value='" + value
                + "' has been successfully deactivated")).build();
    }

    public void setBlackListManager(final IBlackListManager blackListManager) {
        this.blackListManager = blackListManager;
    }
}
