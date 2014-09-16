package ru.forxy.fraud.rest.v1

import ru.forxy.common.pojo.StatusEntity
import ru.forxy.common.rest.AbstractService
import ru.forxy.fraud.logic.blacklist.IBlackListManager
import ru.forxy.fraud.rest.v1.list.BlackListItem

import javax.ws.rs.*
import javax.ws.rs.core.*

@Path('/')
@Produces(MediaType.APPLICATION_JSON)
class BlackListsEndpoint extends AbstractService {

    IBlackListManager blackListManager

    @GET
    @Path('/blacklists/')
    Response getBlackLists(@QueryParam('type') final String type,
                           @QueryParam('value') final String value,
                           @Context final UriInfo uriInfo,
                           @Context final HttpHeaders headers) {
        respondWith(blackListManager.getMoreItemsFrom(type, value), uriInfo, headers).build()
    }

    @GET
    @Path('/blacklist/')
    Response getBlackListItem(@QueryParam('type') final String type,
                              @QueryParam('value') final String value,
                              @Context final UriInfo uriInfo,
                              @Context final HttpHeaders headers) {
        respondWith(blackListManager.get(type, value), uriInfo, headers).build()
    }

    @POST
    @Path('/blacklist/')
    @Consumes(MediaType.APPLICATION_JSON)
    Response addToBlackList(final BlackListItem blackListItem,
                            @Context final UriInfo uriInfo,
                            @Context final HttpHeaders headers) {
        blackListManager.add(blackListItem)
        Response.ok(new StatusEntity('200', "$uriInfo.absolutePath" +
                "?type=$blackListItem.key.type" +
                "&value=$blackListItem.key.value")).build()
    }

    @PUT
    @Path('/blacklist/')
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateBlackList(final BlackListItem blackListItem,
                             @Context final UriInfo uriInfo,
                             @Context final HttpHeaders headers) {
        blackListManager.update(blackListItem)
        Response.ok(new StatusEntity('200', "$uriInfo.absolutePath" +
                "?type=$blackListItem.key.type" +
                "&value=$blackListItem.key.value")).build()
    }

    @DELETE
    @Path('/blacklist/')
    Response deleteBlackList(@QueryParam('type') final String type,
                             @QueryParam('value') final String value,
                             @Context final UriInfo uriInfo,
                             @Context final HttpHeaders headers) {
        blackListManager.delete(new BlackListItem())
        Response.ok(new StatusEntity('200',
                "BlackList item with type='$type' and value='$value' has been successfully deactivated")).build()
    }
}
