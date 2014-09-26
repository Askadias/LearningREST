package fraud.rest.v1

import common.pojo.SortDirection
import common.pojo.StatusEntity
import common.rest.AbstractService
import fraud.logic.velocity.IVelocityConfigManager
import fraud.rest.v1.velocity.VelocityConfig

import javax.ws.rs.*
import javax.ws.rs.core.*

import static javax.ws.rs.core.Response.ok

@Path('/velocity_config/')
@Produces(MediaType.APPLICATION_JSON)
class VelocityConfigEndpoint extends AbstractService {

    IVelocityConfigManager velocityConfigManager

    @GET
    Response getVelocityConfigs(@QueryParam('page') final Integer page,
                                @QueryParam('size') final Integer size,
                                @QueryParam('sort_dir') final SortDirection sortDirection,
                                @QueryParam('sorted_by') final String sortedBy,
                                @QueryParam('metric_type') final String metricType,
                                @QueryParam('updated_by') final String updatedByFilter,
                                @QueryParam('created_by') final String createdByFilter,
                                @Context final UriInfo uriInfo,
                                @Context final HttpHeaders headers) {
        respondWith(page == null && size == null ?
                velocityConfigManager.allVelocityConfigs :
                velocityConfigManager.getVelocityConfigs(page, size, sortDirection, sortedBy,
                        new VelocityConfig(metricType: metricType, updatedBy: updatedByFilter, createdBy: createdByFilter)),
                uriInfo, headers).build()
    }

    @GET
    @Path('/{metric_type}/')
    Response getVelocityConfig(@PathParam('metric_type') final String velocityConfigID,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        respondWith(velocityConfigManager.getVelocityConfig(velocityConfigID), uriInfo, headers).build()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    Response addVelocityConfig(final VelocityConfig velocityConfig,
                               @Context final UriInfo uriInfo,
                               @Context final HttpHeaders headers) {
        velocityConfigManager.createVelocityConfig(velocityConfig)
        ok(new StatusEntity('200', "$uriInfo.absolutePath/$velocityConfig.metricType")).build()
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateVelocityConfig(final VelocityConfig velocityConfig,
                                  @Context final UriInfo uriInfo,
                                  @Context final HttpHeaders headers) {
        velocityConfigManager.updateVelocityConfig(velocityConfig)
        ok(new StatusEntity('200', "$uriInfo.absolutePath/$velocityConfig.metricType")).build()
    }

    @DELETE
    @Path('/{metric_type}/')
    Response deleteVelocityConfig(@PathParam('metric_type') final String velocityConfigID,
                                  @Context final UriInfo uriInfo,
                                  @Context final HttpHeaders headers) {
        velocityConfigManager.deleteVelocityConfig(velocityConfigID)
        ok(new StatusEntity('200', "VelocityConfig for '$velocityConfigID' has been successfully removed")).build()
    }
}
