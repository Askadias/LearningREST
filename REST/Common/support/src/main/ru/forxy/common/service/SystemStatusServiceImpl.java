package ru.forxy.common.service;

import ru.forxy.common.status.ISystemStatusFacade;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * System status service endpoint implementation
 */
@Path("/status/")
@Produces(MediaType.APPLICATION_JSON)
public class SystemStatusServiceImpl extends AbstractService implements ISystemStatusService {

    private ISystemStatusFacade systemStatusFacade;

    @GET
    @Override
    public Response getSystemStatus(@Context UriInfo uriInfo, @Context HttpHeaders headers) {
        return respondWith(systemStatusFacade.getStatus(), uriInfo, headers).build();
    }

    public void setSystemStatusFacade(ISystemStatusFacade systemStatusFacade) {
        this.systemStatusFacade = systemStatusFacade;
    }
}
