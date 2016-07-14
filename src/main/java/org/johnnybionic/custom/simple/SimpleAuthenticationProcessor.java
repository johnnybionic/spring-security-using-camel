package org.johnnybionic.custom.simple;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Camel processor that invokes an AuthenticationProvider.
 * 
 * @author johnny
 *
 */
@Slf4j
@Component("authenticationProcessor")
public class SimpleAuthenticationProcessor implements Processor {

	private static final String AUTHENTICATION = "authentication";
	private AuthenticationProvider authenticationProvider;

	@Autowired
	public SimpleAuthenticationProcessor(
			@Qualifier("camelRouteAuthenticationProvider") AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Authentication body = exchange.getIn().getBody(Authentication.class);
		log.info("Authenticating [{}]", body);
		Authentication authenticate = authenticationProvider.authenticate(body);
		// place the response into a header, in case subsequent stages require it
		exchange.getIn().setHeader(AUTHENTICATION, authenticate);
	}
	
}
