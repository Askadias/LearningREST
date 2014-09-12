package ru.forxy.fraud.rest.v1;

import ru.forxy.common.rest.AbstractService;
import ru.forxy.fraud.logic.velocity.IVelocityManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.Map;

@Path("/velocity/")
@Produces(MediaType.APPLICATION_JSON)
public class VelocityEndpoint extends AbstractService {

    private IVelocityManager velocityManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response check(final Map<String, String> metrics,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        return respondWith(velocityManager.check(metrics), uriInfo, headers).build();
    }

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
                                @QueryParam("related_metric_type") final String relatedMetricType,
                                @QueryParam("create_date") final Long createDateMillis,
                                @Context final UriInfo uriInfo,
                                @Context final HttpHeaders headers) {
        Date createDate = createDateMillis != null ? new Date(createDateMillis) : null;
        return respondWith(velocityManager.getMoreDataFrom(metricType, metricValue, relatedMetricType, createDate),
                uriInfo, headers).build();
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
