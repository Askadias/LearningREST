package ru.forxy.auth.logic;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.auth.pojo.Client;

/**
 * Entry point into auth service business logic
 */
public interface IClientServiceFacade {

    Iterable<Client> getAllClients();

    EntityPage<Client> getClients(final Integer page, final Integer size, final SortDirection sortDirection,
                                  final String sortedBy, final Client filter);

    Client getClient(final String clientID);

    void updateClient(final Client auth);

    void createClient(final Client auth);

    void deleteClient(final String clientID);
}
