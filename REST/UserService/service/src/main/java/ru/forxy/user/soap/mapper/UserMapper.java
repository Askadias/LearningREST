package ru.forxy.user.soap.mapper;

import ru.forxy.common.pojo.EntityPage;
import ru.forxy.common.utils.DateUtils;
import ru.forxy.user.rest.v1.pojo.User;
import ru.forxy.user.soap.gen.v1.UserPageType;
import ru.forxy.user.soap.gen.v1.UserType;

/**
 * User Mapping helper
 */
public abstract class UserMapper {

    public static UserPageType fromUserPage(final EntityPage<User> userPage) {
        if (userPage == null) {
            return null;
        }
        final UserPageType userPageType = new UserPageType();
        userPageType.setNumber(userPage.getNumber());
        userPageType.setSize(userPage.getSize());
        userPageType.setTotal(userPage.getTotal());
        for (final User user : userPage.getContent()) {
            userPageType.getUser().add(fromUser(user));
        }
        return userPageType;
    }

    public static User toUser(final UserType userType) {
        if (userType == null) {
            return null;
        }
        final User user = new User(userType.getEmail(), userType.getPassword());
        user.setLogin(userType.getLogin());
        user.setFirstName(userType.getFirstName());
        user.setLastName(userType.getFirstName());
        user.setGender(userType.getGender() != null ? userType.getGender().charAt(0) : null);
        try {
            user.setBirthDate(DateUtils.dateFromXMLGregorianCalendar(userType.getBirthDate()));
        } catch (final Exception ignored) {
        }
        return user;
    }

    public static UserType fromUser(final User user) {
        if (user == null) {
            return null;
        }
        final UserType userType = new UserType();
        userType.setEmail(user.getEmail());
        userType.setPassword(user.getPassword());
        userType.setLogin(user.getLogin());
        userType.setFirstName(user.getFirstName());
        userType.setLastName(user.getLastName());
        userType.setGender(user.getGender().toString());
        try {
            userType.setBirthDate(DateUtils.newXMLGregorianCalendar(user.getBirthDate()));
        } catch (final Exception ignored) {
        }
        return userType;
    }
}
