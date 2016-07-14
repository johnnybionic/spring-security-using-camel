package org.johnnybionic.custom.camel;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * An authentication provider that invokes a Camel route.
 * 
 * @author johnny
 *
 */
@Slf4j
@Component("camelAuthenticationProvider")
public class CamelAuthenticationProvider implements AuthenticationProvider {

	private ProducerTemplate producerTemplate;
	
	@Setter
    @Value("${authentication.route:direct:authentication-route}")
    private String authenticationRoute;
	
	@Autowired
	public CamelAuthenticationProvider(ProducerTemplate producerTemplate) {
		this.producerTemplate = producerTemplate;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		try {
			Authentication response = producerTemplate.requestBody(authenticationRoute, authentication, Authentication.class);
			
			return response;
		}
		catch (CamelExecutionException camelExecutionException) {
			if (camelExecutionException.getCause() instanceof AuthenticationException) {
				throw ((AuthenticationException) camelExecutionException.getCause());
			}
			
			throw camelExecutionException;
		}
			
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
