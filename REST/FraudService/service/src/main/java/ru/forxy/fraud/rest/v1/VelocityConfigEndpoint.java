package ru.forxy.fraud.rest.v1;

import ru.forxy.common.pojo.SortDirection;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.fraud.logic.velocity.IVelocityConfigManager;
import ru.forxy.fraud.rest.v1.velocity.VelocityConfig;

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

@Path("/velocity_config/")
@Produces(MediaType.APPLICATION_JSON)
public class VelocityConfigEndpoint extends AbstractService {

    private IVelocityConfigManager velocityConfigManager;

    @GET
    public Response getVelocityConfigs(@QueryParam("page") final Integer page,
                                       @QueryParam("size") final Integer size,
                                       @QueryParam("sort_dir") final SortDirection sortDirection,
                                       @QueryParam("sorted_by") final String sortedBy,
                                       @QueryParam("metric_type") final String metricType,
                                       @QueryParam("updated_by") final String updatedByFilter,
                                       @QueryParam("created_by") final String createdByFilter,
                                       @Context final UriInfo uriInfo,
                                       @Context final HttpHeaders headers) {
        return respondWith(page == null && size == null ?
                        velocityConfigManager.getAllVelocityConfigs() :
                        velocityConfigManager.getVelocityConfigs(page, size, sortDirection, sortedBy,
                                new VelocityConfig(metricType, updatedByFilter, createdByFilter)),
                uriInfo, headers).build();
    }

    @GET
    @Path("/{metric_type}/")
    public Response getVelocityConfig(@PathParam("metric_type") final String velocityConfigID,
                                      @Context final UriInfo uriInfo,
                                      @Context final HttpHeaders headers) {
        return respondWith(velocityConfigManager.getVelocityConfig(velocityConfigID), uriInfo, headers).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerVelocityConfig(final VelocityConfig velocityConfig,
                                           @Context final UriInfo uriInfo,
                                           @Context final HttpHeaders headers) {
        velocityConfigManager.createVelocityConfig(velocityConfig);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/"
                + velocityConfig.getMetricType())).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateVelocityConfig(final VelocityConfig velocityConfig,
                                         @Context final UriInfo uriInfo,
                                         @Context final HttpHeaders headers) {
        velocityConfigManager.updateVelocityConfig(velocityConfig);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/"
                + velocityConfig.getMetricType())).build();
    }

    @DELETE
    @Path("/{metric_type}/")
    public Response deleteVelocityConfig(@PathParam("metric_type") final String velocityConfigID,
                                         @Context final UriInfo uriInfo,
                                         @Context final HttpHeaders headers) {
        velocityConfigManager.deleteVelocityConfig(velocityConfigID);
        return Response.ok(new StatusEntity("200",
                "VelocityConfig for metric '" + velocityConfigID + "' has been successfully removed")).build();
    }

    public void setVelocityConfigManager(final IVelocityConfigManager velocityConfigManager) {
        this.velocityConfigManager = velocityConfigManager;
    }
}
