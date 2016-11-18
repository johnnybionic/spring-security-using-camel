package org.johnnybionic.custom.simple;

import java.util.Collection;
import java.util.HashSet;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Adds a Collection of roles to the Exchange. ROLE_USER is always added, and
 * ROLE_ADMIN if the user has administration rights.
 *
 * @author johnny
 *
 */
@Slf4j
@Component("roleProcessor")
public class SimpleRoleProcessor implements Processor {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_EXTRA = "ROLE_EXTRA";
    private static final String ADMIN = "admin";
    private static final String EXTRA = "extra";
    private static final String ROLES = "roles";

    @Override
    public void process(final Exchange exchange) throws Exception {
        Authentication body = exchange.getIn().getBody(Authentication.class);

        // create from the existing User, in case a previous processor added a
        // role
        // - using a Set prevents duplicates (e.g. if previous process added the
        // same roles)
        Collection<GrantedAuthority> roles = new HashSet<>(body.getAuthorities());

        // always add a ROLE_USER
        roles.add(new SimpleGrantedAuthority(ROLE_USER));

        if (ADMIN.equalsIgnoreCase(body.getName()) && ADMIN.equalsIgnoreCase(body.getCredentials().toString())) {
            log.info("Admin user detected");
            roles.add(new SimpleGrantedAuthority(ROLE_ADMIN));
        }

        if (EXTRA.equalsIgnoreCase(body.getName()) && EXTRA.equalsIgnoreCase(body.getCredentials().toString())) {
            log.info("User with extra role detected");
            roles.add(new SimpleGrantedAuthority(ROLE_EXTRA));
        }

        exchange.getIn().setHeader(ROLES, roles);
    }

}
