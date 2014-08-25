package ru.forxy.auth.rest.support;

import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import ru.forxy.auth.logic.IGroupManager;
import ru.forxy.auth.logic.IUserManager;
import ru.forxy.auth.rest.v1.pojo.Group;
import ru.forxy.auth.rest.v1.pojo.User;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private HttpHeaders headers;

    private IUserManager userManager;

    private IGroupManager groupManager;

    private List<String> exclude;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (exclude != null && exclude.contains(requestContext.getUriInfo().getPath())) {
            return;
        }
        SecurityContext sc = requestContext.getSecurityContext();
        if (sc != null) {
            Principal principal = sc.getUserPrincipal();
            if (principal != null) {
                String accountName = principal.getName();

                final User account = userManager.getUser(accountName);
                if (account == null) {
                    requestContext.abortWith(createFaultResponse());
                } else {
                    setNewSecurityContext(requestContext, account);
                    return;
                }
            }
        }
        List<String> authValues = headers.getRequestHeader("Authorization");
        if (authValues == null || authValues.size() != 1) {
            requestContext.abortWith(createFaultResponse());
            return;
        }
        String[] values = authValues.get(0).split(" ");
        if (values.length != 2 || !"Basic".equals(values[0])) {
            requestContext.abortWith(createFaultResponse());
            return;
        }

        String decodedValue;
        try {
            decodedValue = new String(Base64Utility.decode(values[1]));
            String[] namePassword = decodedValue.split(":");
            if (namePassword.length != 2) {
                requestContext.abortWith(createFaultResponse());
                return;
            }
            final User account = userManager.getUser(namePassword[0]);
            if (account == null || !account.getPassword().equals(namePassword[1])) {
                requestContext.abortWith(createFaultResponse());
                return;
            }
            setNewSecurityContext(requestContext, account);

        } catch (Base64Exception ex) {
            requestContext.abortWith(createFaultResponse());
        }
    }

    private void setNewSecurityContext(final ContainerRequestContext requestContext, final User account) {
        requestContext.setSecurityContext(new SecurityContext() {
            private User user = account;
            private Principal userPrincipal = new SimplePrincipal(account.getEmail());

            @Override
            public Principal getUserPrincipal() {
                return userPrincipal;
            }

            @Override
            public boolean isUserInRole(String role) {
                if (user.getGroups() != null) {
                    for (Group group : groupManager.getGroups(user.getGroups())) {
                        if (group.getScopes() != null) {
                            for (Map.Entry<String, List<String>> clientScopes : group.getScopes().entrySet()) {
                                if (clientScopes.getValue().contains(role)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getAuthenticationScheme() {
                return SecurityContext.BASIC_AUTH;
            }
        });
    }

    private Response createFaultResponse() {
        return Response.status(401).header("WWW-Authenticate", "Basic realm=\"Forxy.ru\"").build();
        //return Response.status(301).header("location", "/AuthService/app/login").build();
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }
}
