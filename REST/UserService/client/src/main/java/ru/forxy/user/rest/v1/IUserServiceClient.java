package ru.forxy.user.rest.v1;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.user.rest.v1.pojo.User;

public interface IUserServiceClient {

    EntityPage<User>  getUsers(final String transactionGUID, final Integer page);

    EntityPage<User> getUsers(final String transactionGUID, final Integer page, final Integer size);

    User getUser(final String transactionGUID, final User requestedUser);
}
