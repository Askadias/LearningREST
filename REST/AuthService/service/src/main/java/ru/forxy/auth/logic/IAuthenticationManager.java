package ru.forxy.auth.logic;

import ru.forxy.auth.rest.v1.pojo.Credentials;
import ru.forxy.auth.rest.v1.pojo.Profile;

/**
 * Authentication manager
 */
public interface IAuthenticationManager {

    String login(Credentials credentials);

    Profile getProfile(final String email);
}
