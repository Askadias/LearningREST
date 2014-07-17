package ru.forxy.user.rest.v1;

import ru.forxy.common.pojo.SortDirection;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.common.rest.AbstractService;
import ru.forxy.oauth.pojo.Client;
import ru.forxy.user.logic.IClientServiceFacade;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class ClientServiceImpl extends AbstractService implements IClientService {

    private IClientServiceFacade clientServiceFacade;

    @Override
    public Response getClients(final Integer page, final Integer size, final SortDirection sortDirection,
                               final String sortedBy, final Client filter, final UriInfo uriInfo,
                               final HttpHeaders headers) {
        return respondWith(page == null && size == null ?
                        clientServiceFacade.getAllClients() :
                        clientServiceFacade.getClients(page, size, sortDirection, sortedBy, filter),
                uriInfo, headers).build();
    }

    @Override
    public Response getClient(String clientID, final UriInfo uriInfo, final HttpHeaders headers) {
        return respondWith(clientServiceFacade.getClient(clientID), uriInfo, headers).build();
    }

    @Override
    public Response registerClient(final Client client, final UriInfo uriInfo, final HttpHeaders headers) {
        clientServiceFacade.createClient(client);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + client.getClientID())).build();
    }

    @Override
    public Response updateClient(final Client client, final UriInfo uriInfo, final HttpHeaders headers) {
        clientServiceFacade.updateClient(client);
        return Response.ok(new StatusEntity("200", uriInfo.getAbsolutePath() + "/" + client.getClientID())).build();
    }

    @Override
    public Response deleteClient(final String clientID, final UriInfo uriInfo, final HttpHeaders headers) {
        clientServiceFacade.deleteClient(clientID);
        return Response.ok(new StatusEntity("200",
                "Client with clientID='" + clientID + "' has been successfully removed")).build();
    }

    public void setClientServiceFacade(final IClientServiceFacade clientServiceFacade) {
        this.clientServiceFacade = clientServiceFacade;
    }
}
