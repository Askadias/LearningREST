package auth.logic;

import auth.rest.v1.pojo.Credentials;
import auth.rest.v1.pojo.Profile;

/**
 * Authentication manager
 */
public interface IAuthenticationService {

    String login(Credentials credentials);

    Profile getProfile(final String email);
}
