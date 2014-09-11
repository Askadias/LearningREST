package ru.forxy.fraud.rest.v1;

import ru.forxy.common.rest.AbstractService;
import ru.forxy.fraud.logic.velocity.IVelocityManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    @Path("/metrics/")
    public Response getMetrics(@QueryParam("metric_type") final String metricType,
                               @QueryParam("metric_value") final String metricValue,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getMoreMetricsFrom(metricType, metricValue), uriInfo, headers).build();
    }

    @GET
    @Path("/data_list/")
    public Response getDataList(@QueryParam("metric_type") final String metricType,
                                @QueryParam("metric_value") final String metricValue,
                                @Context final UriInfo uriInfo,
                                @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getMoreDataFrom(metricType, metricValue), uriInfo, headers).build();
    }

    @GET
    @Path("/metric/")
    public Response getMetric(@QueryParam("metric_type") final String metricType,
                              @QueryParam("metric_value") final String metricValue,
                              @Context final UriInfo uriInfo,
                              @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getMetrics(metricType, metricValue), uriInfo, headers).build();
    }

    @GET
    @Path("/data/")
    public Response getData(@QueryParam("metric_type") final String metricType,
                            @QueryParam("metric_value") final String metricValue,
                            @Context final UriInfo uriInfo,
                            @Context final HttpHeaders headers) {
        return respondWith(velocityManager.getDataList(metricType, metricValue), uriInfo, headers).build();
    }

    public void setVelocityManager(IVelocityManager velocityManager) {
        this.velocityManager = velocityManager;
    }
}
