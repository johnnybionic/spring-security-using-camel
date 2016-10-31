package org.johnnybionic.custom.simple;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Shows how a User can be built as the last step of the Camel route, collecting
 * information that's been added to the Exchange during the route's process.
 *
 * @author johnny
 *
 */
@Component("buildUserProcessor")
public class SimpleBuildUserProcessor implements Processor {

    private static final String ROLES = "roles";

    @Override
    public void process(final Exchange exchange) throws Exception {

        Authentication authentication = exchange.getIn().getBody(Authentication.class);

        Collection<GrantedAuthority> roles = exchange.getIn().getHeader(ROLES, Collection.class);
        User user = new User(authentication.getName(), authentication.getCredentials().toString(), roles);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user,
                authentication.getCredentials().toString(), roles);
        exchange.getIn().setBody(token);

    }

}
