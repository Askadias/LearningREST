package fraud.rest.v1

import fraud.logic.velocity.IVelocityManager
import common.rest.AbstractService

import javax.ws.rs.*
import javax.ws.rs.core.*

@Path('/velocity/')
@Produces(MediaType.APPLICATION_JSON)
class VelocityEndpoint extends AbstractService {

    IVelocityManager velocityManager

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response check(final Map<String, String> metrics,
                   @Context final UriInfo uriInfo,
                   @Context final HttpHeaders headers) {
        //respondWith(velocityManager.checkSync(metrics), uriInfo, headers).build()
        //respondWith(velocityManager.checkFJP(metrics), uriInfo, headers).build()
        //respondWith(velocityManager.checkGPars(metrics), uriInfo, headers).build()
        respondWith(velocityManager.checkAsync(metrics), uriInfo, headers).build()
    }

    @POST
    @Path('/rcheck/')
    @Consumes(MediaType.APPLICATION_JSON)
    Response rcheck(final Map<String, String> metrics,
                    @Context final UriInfo uriInfo,
                    @Context final HttpHeaders headers) {
        respondWith(velocityManager.checkRedisGParsAsync(metrics), uriInfo, headers).build()
        //respondWith(velocityManager.checkRedisGPars(metrics), uriInfo, headers).build()
    }

    @GET
    @Path('/metrics/')
    Response getMetrics(@QueryParam('metric_type') final String metricType,
                        @QueryParam('metric_value') final String metricValue,
                        @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers) {
        respondWith(velocityManager.getMoreMetricsFrom(metricType, metricValue), uriInfo, headers).build()
    }

    @GET
    @Path('/data_list/')
    Response getDataList(@QueryParam('metric_type') final String metricType,
                         @QueryParam('metric_value') final String metricValue,
                         @QueryParam('related_metric_type') final String relatedMetricType,
                         @QueryParam('create_date') final Long createDateMillis,
                         @Context final UriInfo uriInfo,
                         @Context final HttpHeaders headers) {
        Date createDate = createDateMillis != null ? new Date(createDateMillis) : null
        respondWith(velocityManager.getMoreDataFrom(metricType, metricValue, relatedMetricType, createDate),
                uriInfo, headers).build()
    }

    @GET
    @Path('/metric/')
    Response getMetric(@QueryParam('metric_type') final String metricType,
                       @QueryParam('metric_value') final String metricValue,
                       @Context final UriInfo uriInfo,
                       @Context final HttpHeaders headers) {
        respondWith(velocityManager.getMetrics(metricType, metricValue), uriInfo, headers).build()
    }

    @GET
    @Path('/data/')
    Response getData(@QueryParam('metric_type') final String metricType,
                     @QueryParam('metric_value') final String metricValue,
                     @Context final UriInfo uriInfo,
                     @Context final HttpHeaders headers) {
        respondWith(velocityManager.getDataList(metricType, metricValue), uriInfo, headers).build()
    }
}
