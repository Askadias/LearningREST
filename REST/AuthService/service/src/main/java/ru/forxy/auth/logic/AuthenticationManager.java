package ru.forxy.auth.logic;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.forxy.auth.db.dao.IProfileDAO;
import ru.forxy.auth.db.dao.IUserDAO;
import ru.forxy.auth.exceptions.AuthServiceEventLogId;
import ru.forxy.auth.rest.v1.pojo.Credentials;
import ru.forxy.auth.rest.v1.pojo.Profile;
import ru.forxy.auth.rest.v1.pojo.User;
import ru.forxy.auth.security.IJWTManager;
import ru.forxy.common.exceptions.ServiceException;

/**
 * Authentication Manager implementation
 */
public class AuthenticationManager implements IAuthenticationManager {

    private PasswordEncoder passwordEncoder;

    private IProfileDAO profileDAO;
    private IUserDAO userDAO;

    private IJWTManager jwtManager;

    @Override
    public String login(Credentials credentials) {
        User user = userDAO.findOne(credentials.getEmail());
        if (user != null) {
            if (passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
                try {
                    return jwtManager.toJWT(user);
                } catch (JOSEException e) {
                    throw new ServiceException(e, AuthServiceEventLogId.NotAuthorized, credentials.getEmail());
                }
            }
        }
        throw new ServiceException(AuthServiceEventLogId.NotAuthorized, credentials.getEmail());
    }

    @Override
    public Profile getProfile(final String email) {
        Profile user = profileDAO.findOne(email);
        if (user == null) {
            throw new ServiceException(AuthServiceEventLogId.UserNotFound, email);
        }
        return user;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setProfileDAO(IProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setJwtManager(IJWTManager jwtManager) {
        this.jwtManager = jwtManager;
    }
}
