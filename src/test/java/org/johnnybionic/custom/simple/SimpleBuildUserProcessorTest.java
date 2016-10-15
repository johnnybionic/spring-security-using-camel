package org.johnnybionic.custom.simple;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.johnnybionic.CamelSecurityApplication;
import org.johnnybionic.custom.simple.SimpleBuildUserProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
public class SimpleBuildUserProcessorTest {

	private static final String ROLES = "roles";

	private static final String USER_ROLE = "ROLE_USER";

	private static final Object USERNAME = "user";

	private static final Object PASSWORD = "password";
	
	@Autowired
	private SimpleBuildUserProcessor processor;
	
	@Test
	public void testUserIsBuilt() throws Exception {
		
		CamelContext context = new DefaultCamelContext();
		Exchange exchange = new DefaultExchange(context);
		
		List<GrantedAuthority> roles = new ArrayList<>();
		SimpleGrantedAuthority userRole = new SimpleGrantedAuthority(USER_ROLE);
		roles.add(userRole);
		exchange.getIn().setHeader(ROLES, roles);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
		exchange.getIn().setBody(authentication );

		processor.process(exchange);
		
		Object body = exchange.getIn().getBody();
		assertTrue(body instanceof UsernamePasswordAuthenticationToken);
		
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)body;
		assertTrue(token.getAuthorities().contains(userRole));
		assertEquals(USERNAME, token.getName());
		assertEquals(PASSWORD, token.getCredentials().toString());
		
	}

}
