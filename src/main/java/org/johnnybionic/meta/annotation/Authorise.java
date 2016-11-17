package org.johnnybionic.meta.annotation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Helper method(s) for security annotations.
 *
 * @author johnny
 *
 */
@Component
public class Authorise {

    private static final String EXTRA_ROLE = "ROLE_EXTRA";

    /**
     * Checks if the user has the 'extra' role.
     *
     * @param user the user to check
     * @return true if the user has the role, false otherwise
     */
    public boolean checkUserHasExtraRole(final Authentication user) {
        if (user != null) {
            for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals(EXTRA_ROLE)) {
                    return true;
                }
            }
        }

        return false;
    }
}
