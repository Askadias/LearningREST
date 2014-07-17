package ru.forxy.user.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.oauth.pojo.Client;

/**
 * Entry point into user service business logic
 */
public interface IClientServiceFacade {

    Iterable<Client> getAllClients();

    EntityPage<Client> getClients(final Integer page, final Integer size, final SortDirection sortDirection,
                                  final String sortedBy, final Client filter);

    Client getClient(final String clientID);

    void updateClient(final Client user);

    void createClient(final Client user);

    void deleteClient(final String clientID);
}
