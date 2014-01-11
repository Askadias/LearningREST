package ru.forxy.common.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * System status enpoint
 * You can call it to get service health status
 */
@Path("/status/")
@Produces(MediaType.APPLICATION_JSON)
public interface ISystemStatusService {

    @GET
    Response getSystemStatus(@Context final UriInfo uriInfo, @Context final HttpHeaders headers);
}
