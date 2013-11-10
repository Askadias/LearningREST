package ru.forxy.common.service;

import ru.forxy.common.status.ISystemStatusFacade;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * System status service endpoint implementation
 */
public class SystemStatusServiceImpl extends AbstractService implements ISystemStatusService {

    private ISystemStatusFacade systemStatusFacade;

    @Override
    public Response getSystemStatus(UriInfo uriInfo, HttpHeaders headers) {
        return respondWith(systemStatusFacade.getStatus(), uriInfo, headers).build();
    }

    public void setSystemStatusFacade(ISystemStatusFacade systemStatusFacade) {
        this.systemStatusFacade = systemStatusFacade;
    }
}
