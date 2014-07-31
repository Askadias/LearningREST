package ru.forxy.auth.rest.v1;

import ru.forxy.auth.logic.ITokenManager;
import ru.forxy.auth.rest.v1.pojo.Token;
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

@Path("/tokens/")
@Produces(MediaType.APPLICATION_JSON)
public class TokenGrantServiceEndpoint extends AbstractService {

    private ITokenManager tokenGrantServiceFacade;

    @GET
    public Response getTokens(@QueryParam("page") final Integer page,
                              @QueryParam("size") final Integer size,
                              @QueryParam("sortDir") final SortDirection sortDirection,
                              @QueryParam("sortedBy") final String sortedBy,
                              @QueryParam("") final Token filter,
                              @Context final UriInfo uriInfo,
                              @Context final HttpHeaders headers) {
        return respondWith(page == null && size == null ?
                        tokenGrantServiceFacade.getAllTokens() :
                        tokenGrantServiceFacade.getTokens(page, size, sortDirection, sortedBy, filter),
                uriInfo, headers).build();
    }

    @GET
    @Path("/{tokenID}/")
    public Response getToken(@PathParam("tokenID") final String tokenID,
                             @Context final UriInfo uriInfo,
                             @Context final HttpHeaders headers) {
        return respondWith(tokenGrantServiceFacade.getToken(tokenID), uriInfo, headers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerToken(final Token Token,
                                  @Context final UriInfo uriInfo,
                                  @Context final HttpHeaders headers) {
        tokenGrantServiceFacade.createToken(Token);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + Token.getTokenKey())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateToken(final Token Token,
                                @Context final UriInfo uriInfo,
                                @Context final HttpHeaders headers) {
        tokenGrantServiceFacade.updateToken(Token);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + Token.getTokenKey())).build();
    }

    @DELETE
    @Path("/{tokenID}/")
    public Response deleteToken(@PathParam("tokenID") final String tokenID,
                                @Context final UriInfo uriInfo,
                                @Context final HttpHeaders headers) {
        tokenGrantServiceFacade.deleteToken(tokenID);
        return Response.ok(new StatusEntity("200",
                "Token with tokenID='" + tokenID + "' has been successfully removed")).build();
    }

    public void setTokenManager(ITokenManager tokenManager) {
        this.tokenGrantServiceFacade = tokenManager;
    }
}