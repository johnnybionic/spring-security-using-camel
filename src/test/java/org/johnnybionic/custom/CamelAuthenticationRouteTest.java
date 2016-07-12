package org.johnnybionic.custom;

import static org.junit.Assert.*;

import org.apache.camel.CamelExecutionException;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.johnnybionic.CamelSecurityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebIntegrationTest
public class CamelAuthenticationRouteTest {

	 private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private static final Object BAD_USERNAME = "error";

	private static final Object BAD_PASSWORD = "error";

	@Produce(uri = "direct:authentication-route")
	protected ProducerTemplate template;

	@Test
	public void test() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
		Object response = template.requestBody(authentication);

		assertTrue(response instanceof Authentication);
		Authentication user = ((Authentication)response);
		assertEquals(USERNAME, user.getName());
		assertEquals(PASSWORD, user.getCredentials().toString());
		assertEquals(1, user.getAuthorities().size());
	}

	@Test()
	public void thatAuthenticationExceptionThrowForBadCredentials() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(BAD_USERNAME, BAD_PASSWORD);
		try {
			template.requestBody(authentication);
			fail();
		}
		catch (CamelExecutionException camelExecutionException) {
			assertTrue(camelExecutionException.getCause() instanceof AuthenticationException);
		}
		
	}
}
