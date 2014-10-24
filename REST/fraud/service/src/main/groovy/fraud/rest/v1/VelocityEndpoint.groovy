package fraud.rest.v1

import common.rest.AbstractService
import fraud.logic.velocity.IVelocityManager
import org.joda.time.DateTime

import javax.ws.rs.*
import javax.ws.rs.core.*

@Path('/velocity/')
@Produces(MediaType.APPLICATION_JSON)
class VelocityEndpoint extends AbstractService {

    IVelocityManager velocityManager

    @POST
    @Path('/cassandra/check/')
    @Consumes(MediaType.APPLICATION_JSON)
    Response cassandraCheck(final Map<String, String[]> metrics,
                            @Context final UriInfo uriInfo,
                            @Context final HttpHeaders headers) {
        respondWith(velocityManager.cassandraGetMetrics(metrics, true), uriInfo, headers).build()
    }

    @POST
    @Path('/redis/check/')
    @Consumes(MediaType.APPLICATION_JSON)
    Response redisCheck(final Map<String, String[]> metrics,
                        @Context final UriInfo uriInfo,
                        @Context final HttpHeaders headers) {
        respondWith(velocityManager.redisGetMetrics(metrics, true), uriInfo, headers).build()
    }

    @GET
    @Path('/cassandra/metrics/')
    Response getCassandraMetrics(@Context final UriInfo uriInfo,
                                 @Context final HttpHeaders headers) {
        respondWith(velocityManager.cassandraGetMetrics(toMetrics(uriInfo), false), uriInfo, headers).build()
    }

    @GET
    @Path('/redis/metrics/')
    Response getRedisMetrics(@Context final UriInfo uriInfo,
                             @Context final HttpHeaders headers) {
        respondWith(velocityManager.redisGetMetrics(toMetrics(uriInfo), false), uriInfo, headers).build()
    }

    @GET
    @Path('/cassandra/history/')
    Response getCassandraHistoricalData(@QueryParam('start_date') String startDate,
                                        @QueryParam('end_date') String endDate,
                                        @Context final UriInfo uriInfo,
                                        @Context final HttpHeaders headers) {
        respondWith(velocityManager.cassandraGetHistory(
                toMetricsFilter(uriInfo),
                startDate ? DateTime.parse(startDate) : null,
                endDate ? DateTime.parse(endDate) : null
        ), uriInfo, headers).build()
    }

    @GET
    @Path('/redis/history/')
    Response getRedisHistoricalData(@QueryParam('start_date') String startDate,
                                    @QueryParam('end_date') String endDate,
                                    @Context final UriInfo uriInfo,
                                    @Context final HttpHeaders headers) {
        respondWith(velocityManager.redisGetHistory(
                toMetricsFilter(uriInfo),
                startDate ? DateTime.parse(startDate) : null,
                endDate ? DateTime.parse(endDate) : null
        ), uriInfo, headers).build()
    }

    private static Map<String, String[]> toMetrics(UriInfo uriInfo) {
        def metrics = [:]
        uriInfo.getQueryParameters()?.each { metric, values ->
            if (metric != 'start_date' && metric != 'end_date') {
                metrics << [(metric): values.toArray()]
            }
        }
        return metrics
    }

    private static Map<String, String> toMetricsFilter(UriInfo uriInfo) {
        def filter = [:]
        uriInfo.getQueryParameters()?.each { metric, values ->
            if (values.size() > 0 && metric != 'start_date' && metric != 'end_date') {
                filter << [(metric): values.first()]
            }
        }
        return filter
    }
}
