package org.johnnybionic.custom.camel;

import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A simple Camel route that authenticates the user, adds roles, and finally
 * builds a User.
 *
 * @author johnny
 *
 */
@Component
public class CamelAuthenticationRoute extends RouteBuilder {

    @Value("${authentication.route:direct:authentication-route}")
    private String authenticationRoute;

    @Autowired
    @Qualifier("authenticationProcessor")
    private Processor authProcessor;

    @Autowired
    @Qualifier("roleProcessor")
    private Processor roleProcessor;

    @Autowired
    @Qualifier("buildUserProcessor")
    private Processor buildUser;

    @Override
    public void configure() throws Exception {

        // this can be used to transform all AuthenticationExceptions to one
        // single obfuscated message
        // onException(AuthenticationException.class)
        // .log(LoggingLevel.INFO, "Caught AuthenticationException")
        // .throwException(new BadCredentialsException("Invalid credentials"))
        // .end();

        from(authenticationRoute).log(LoggingLevel.INFO, "Authenticating ... ")

                // authenticate the user
                .bean(authProcessor)
                // add the user's roles to the exchange
                .bean(roleProcessor)
                // build the user from the output of the previous stages
                .bean(buildUser);
    }

}
