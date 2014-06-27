package ru.forxy.user.rest.v1;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.StatusEntity;
import ru.forxy.user.rest.v1.pojo.User;

import java.util.List;

public interface IUserServiceClient {

    List getUsers(final String transactionGUID);

    EntityPage<User> getUsers(final String transactionGUID, final Integer page);

    EntityPage<User> getUsers(final String transactionGUID, final Integer page, final Integer size);

    User getUser(final String transactionGUID, final String email);

    StatusEntity createUser(final String transactionGUID, final User user);

    StatusEntity updateUser(final String transactionGUID, final User user);

    StatusEntity deleteUser(final String transactionGUID, final String email);
}
