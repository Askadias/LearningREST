package ru.forxy.auth.logic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.forxy.common.exceptions.ServiceException;
import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.pojo.SortDirection;
import ru.forxy.auth.db.dao.IUserDAO;
import ru.forxy.auth.exceptions.AuthServiceEventLogId;
import ru.forxy.auth.rest.v1.pojo.Credentials;
import ru.forxy.auth.rest.v1.pojo.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation class for UserService business logic
 */
public class UserManager implements IUserManager {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private IUserDAO userDAO;

    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        List<User> allUsers = new LinkedList<User>();
        for (User user : userDAO.findAll()) {
            allUsers.add(user);
        }
        return allUsers;
    }

    @Override
    public EntityPage<User> getUsers(final Integer page, final Integer size, final SortDirection sortDirection,
                                     final String sortedBy, final User filter) {
        if (page >= 1) {
            int pageSize = size == null ? DEFAULT_PAGE_SIZE : size;
            PageRequest pageRequest;
            if (sortDirection != null && sortedBy != null) {
                Sort.Direction dir = sortDirection == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
                pageRequest = new PageRequest(page - 1, pageSize, dir, sortedBy);
            } else {
                pageRequest = new PageRequest(page - 1, pageSize);
            }
            final Page<User> p = userDAO.findAll(pageRequest, filter);
            return new EntityPage<>(p.getContent(), p.getSize(), p.getNumber(), p.getTotalElements());
        } else {
            throw new ServiceException(AuthServiceEventLogId.InvalidPageNumber, page);
        }
    }

    @Override
    public User getUser(final String email) {
        User user = userDAO.findOne(email);
        if (user == null) {
            throw new ServiceException(AuthServiceEventLogId.UserNotFound, email);
        }
        return user;
    }

    @Override
    public void updateUser(final User user) {
        if (userDAO.exists(user.getEmail())) {
            userDAO.save(user);
        } else {
            throw new ServiceException(AuthServiceEventLogId.UserNotFound, user.getEmail());
        }
    }

    @Override
    public User createUser(final User user) {
        if (!userDAO.exists(user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userDAO.save(user);
        } else {
            throw new ServiceException(AuthServiceEventLogId.UserAlreadyExists, user.getEmail());
        }
    }

    @Override
    public void deleteUser(final String email) {
        if (userDAO.exists(email)) {
            userDAO.delete(email);
        } else {
            throw new ServiceException(AuthServiceEventLogId.UserNotFound, email);
        }
    }

    public void setUserDAO(final IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setPasswordEncoder(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
