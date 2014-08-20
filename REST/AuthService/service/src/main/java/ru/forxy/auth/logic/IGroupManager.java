package ru.forxy.auth.logic;

import ru.forxy.auth.rest.v1.pojo.Group;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;

/**
 * Entry point into auth service business logic
 */
public interface IGroupManager {

    Iterable<Group> getAllGroups();

    EntityPage<Group> getGroups(final Integer page, final Integer size, final SortDirection sortDirection,
                                  final String sortedBy, final Group filter);

    Group getGroup(final String clientID);

    void updateGroup(final Group auth);

    void createGroup(final Group auth);

    void deleteGroup(final String clientID);
}
