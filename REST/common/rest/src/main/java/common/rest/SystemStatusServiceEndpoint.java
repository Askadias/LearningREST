package common.rest;

import common.status.ISystemStatusFacade;

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
@Path("/common/status/")
@Produces(MediaType.APPLICATION_JSON)
public class SystemStatusServiceEndpoint extends AbstractService {

    private ISystemStatusFacade systemStatusFacade;

    @GET
    public Response getSystemStatus(@Context final UriInfo uriInfo,
                                    @Context final HttpHeaders headers) {
        return respondWith(systemStatusFacade.getStatus(), uriInfo, headers).build();
    }

    public void setSystemStatusFacade(final ISystemStatusFacade systemStatusFacade) {
        this.systemStatusFacade = systemStatusFacade;
    }
}
