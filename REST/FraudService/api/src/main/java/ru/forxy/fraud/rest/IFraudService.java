package ru.forxy.fraud.rest;

import ru.forxy.fraud.rest.pojo.Transaction;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface IFraudService {

    @GET
	@Path("/check/")
    Response check(final Transaction transaction, @Context final UriInfo uriInfo, @Context final HttpHeaders headers);
	
	@GET
	@Path("/transactions/{page}/")
	Response getTransactions(@PathParam("page") final Integer page, final UriInfo uriInfo, final HttpHeaders headers);
	
	@GET
	@Path("/transactions/{page}/{size}/")
	Response getTransactions(@PathParam("page") final Integer page, @PathParam("size") final Integer size, final UriInfo uriInfo, final HttpHeaders headers);
	
	@GET
	@Path("/transaction/{id}/")
	Response getTransaction(Long id, final UriInfo uriInfo, final HttpHeaders headers);
}
