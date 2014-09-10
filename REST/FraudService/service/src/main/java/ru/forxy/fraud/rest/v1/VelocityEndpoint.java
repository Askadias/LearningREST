package ru.forxy.fraud.rest.v1;

import ru.forxy.common.rest.AbstractService;
import ru.forxy.fraud.logic.velocity.IVelocityManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/velocity/")
@Produces(MediaType.APPLICATION_JSON)
public class VelocityEndpoint extends AbstractService {

    private IVelocityManager velocityManager;

    @GET
    @Path("/metrics/{metric_type}/{value}")
    public Response getMetrics(@PathParam("metric_type") final String type,
                               @PathParam("value") final String value,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getMoreMetricsFrom(type, value), uriInfo, headers).build();
    }

    @GET
    @Path("/data_list/{metric_type}/{value}")
    public Response getDataList(@PathParam("metric_type") final String type,
                                @PathParam("value") final String value,
                                @Context final UriInfo uriInfo,
                                @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getMoreMetricsFrom(type, value), uriInfo, headers).build();
    }

    @GET
    @Path("/metric/{metric_type}/{value}")
    public Response getMetric(@PathParam("metric_type") final String type,
                              @PathParam("value") final String value,
                              @Context final UriInfo uriInfo,
                              @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getMetric(type, value), uriInfo, headers).build();
    }

    @GET
    @Path("/data/{metric_type}/{value}")
    public Response getData(@PathParam("metric_type") final String type,
                            @PathParam("value") final String value,
                            @Context final UriInfo uriInfo,
                            @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getData(type, value), uriInfo, headers).build();
    }

    public void setVelocityManager(IVelocityManager velocityManager) {
        this.velocityManager = velocityManager;
    }
}
