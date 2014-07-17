package ru.forxy.user.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.oauth.pojo.Client;
import ru.forxy.user.db.dao.IClientDAO;
import ru.forxy.user.exceptions.UserServiceEventLogId;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation class for ClientService business logic
 */
public class ClientServiceFacade implements IClientServiceFacade {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IClientDAO clientDAO;

    public List<Client> getAllClients() {
        List<Client> allClients = new LinkedList<Client>();
        for (Client client : clientDAO.findAll()) {
            allClients.add(client);
        }
        return allClients;
    }

    @Override
    public EntityPage<Client> getClients(final Integer page, final Integer size, final SortDirection sortDirection,
                                         final String sortedBy, final Client filter) {
        if (page >= 1) {
            int pageSize = size == null ? DEFAULT_PAGE_SIZE : size;
            PageRequest pageRequest;
            if (sortDirection != null && sortedBy != null) {
                Sort.Direction dir = sortDirection == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
                pageRequest = new PageRequest(page - 1, pageSize, dir, sortedBy);
            } else {
                pageRequest = new PageRequest(page - 1, pageSize);
            }
            final Page<Client> p = clientDAO.findAll(pageRequest, filter);
            return new EntityPage<>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
        } else {
            throw new ServiceException(UserServiceEventLogId.InvalidPageNumber, page);
        }
    }

    @Override
    public Client getClient(final String clientID) {
        Client client = clientDAO.findOne(clientID);
        if (client == null) {
            throw new ServiceException(UserServiceEventLogId.ClientNotFound, clientID);
        }
        return client;
    }

    @Override
    public void updateClient(final Client client) {
        if (clientDAO.exists(client.getClientID())) {
            clientDAO.save(client);
        } else {
            throw new ServiceException(UserServiceEventLogId.ClientNotFound, client.getClientID());
        }
    }

    @Override
    public void createClient(final Client client) {
        if (!clientDAO.exists(client.getClientID())) {
            clientDAO.save(client);
        } else {
            throw new ServiceException(UserServiceEventLogId.ClientAlreadyExists, client.getClientID());
        }
    }

    @Override
    public void deleteClient(final String clientID) {
        if (clientDAO.exists(clientID)) {
            clientDAO.delete(clientID);
        } else {
            throw new ServiceException(UserServiceEventLogId.ClientNotFound, clientID);
        }
    }

    public void setClientDAO(final IClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }
}
