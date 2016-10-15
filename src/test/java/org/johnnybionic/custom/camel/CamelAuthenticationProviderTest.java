package org.johnnybionic.custom.camel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.camel.ProducerTemplate;
import org.johnnybionic.custom.camel.CamelAuthenticationProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Simple test case - shows how to make things faster by using mocks, without loading the entire
 * web context. As the provider class is quite small and is simply a wrapper, mocking is perfect for testing.
 * 
 * @author johnny
 *
 */
public class CamelAuthenticationProviderTest {

	private static final Object USERNAME = "user";
	private static final Object PASSWORD = "password";
	private static final String AUTHENTICATION_ROUTE = "direct:route";

	@InjectMocks
	private CamelAuthenticationProvider provider;
	
	@Mock
	private ProducerTemplate producerTemplate;
	
	@Mock
	private Authentication authentication;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		provider.setAuthenticationRoute(AUTHENTICATION_ROUTE);
	}

	/**
	 * Ensures the route is called correctly.
	 */
	@Test
	public void testHappyPath() {
		
		Authentication response = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
		when(producerTemplate.requestBody(AUTHENTICATION_ROUTE, authentication, Authentication.class)).thenReturn(response );
		Authentication authenticate = provider.authenticate(authentication);
		verify(producerTemplate).requestBody(AUTHENTICATION_ROUTE, authentication, Authentication.class);
		assertEquals(USERNAME, authenticate.getName());		
	}

	/**
	 * Camel wraps any exceptions.
	 */
	@Test
	public void thatAuthenticationExceptionIsTransformed() {

		when(producerTemplate.requestBody(AUTHENTICATION_ROUTE, authentication, Authentication.class)).thenThrow(new BadCredentialsException("invalid credenitals"));

		try {
			provider.authenticate(authentication);
		} 
		catch (AuthenticationException authenticationException) {
			// expected
		}
		catch (Exception exception) {
			fail();
		}
	}

	/**
	 * This simply ensures no-one changes this method inadvertently
	 */
	@Test
	public void supports() {
		assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
	}
}
