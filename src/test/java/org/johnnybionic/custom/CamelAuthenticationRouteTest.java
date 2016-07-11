package org.johnnybionic.custom;

import static org.junit.Assert.*;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.johnnybionic.CamelSecurityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
public class CamelAuthenticationRouteTest {

	 private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	@Produce(uri = "direct:authentication-route")
	protected ProducerTemplate template;

	@Test
	public void test() {
		Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
		Object response = template.requestBody(authentication);

		assertTrue(response instanceof Authentication);
		
	}

}
