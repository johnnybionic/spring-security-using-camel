package org.johnnybionic.custom.simple;

import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * Shows how to use a different AuthenticationProvider by using profiles. 
 * 
 * @author johnny
 *
 */
@Profile("secondary")
@Component("camelRouteAuthenticationProvider")
public class AlternateSimpleAuthenticationProvider implements AuthenticationProvider {


	private static final String SECONDARY_USER = "second";

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		if (!SECONDARY_USER.equals(authentication.getName())) {
			throw new BadCredentialsException("Invalid credentials");
		}
		
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
