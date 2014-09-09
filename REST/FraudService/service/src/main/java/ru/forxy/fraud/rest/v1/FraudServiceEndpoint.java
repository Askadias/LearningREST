package ru.forxy.fraud.rest.v1;

import ru.forxy.common.pojo.SortDirection;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.fraud.logic.IFraudServiceFacade;
import ru.forxy.fraud.rest.v1.check.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/check")
@Produces(MediaType.APPLICATION_JSON)
public class FraudServiceEndpoint extends AbstractService {

    private IFraudServiceFacade fraudServiceFacade;

    @GET
    public Response check(final Transaction transaction, final UriInfo uriInfo, final HttpHeaders headers) {
        return Response.ok(fraudServiceFacade.check(transaction)).build();
    }

    @GET
    @Path("/transactions/")
    public Response getTransactions(@QueryParam("page") final Integer page,
                                    @QueryParam("size") final Integer size,
                                    @QueryParam("sort_dir") final SortDirection sortDirection,
                                    @QueryParam("sorted_by") final String sortedBy,
                                    @QueryParam("transaction_id") final String idFilter,
                                    @Context final UriInfo uriInfo,
                                    @Context final HttpHeaders headers) {
        return respondWith(page == null && size == null ?
                        fraudServiceFacade.getAllTransactions() :
                        fraudServiceFacade.getTransactions(page, size, sortDirection, sortedBy,
                                new Transaction()),
                uriInfo, headers).build();
    }

    @GET
    @Path("/transactions/{transaction_id}/")
    public Response getTransaction(@PathParam("transaction_id") final String transactionID,
                                   @Context final UriInfo uriInfo,
                                   @Context final HttpHeaders headers) {
        return respondWith(fraudServiceFacade.getTransaction(transactionID), uriInfo, headers).build();
    }

    public void setFraudServiceFacade(final IFraudServiceFacade fraudServiceFacade) {
        this.fraudServiceFacade = fraudServiceFacade;
    }
}
