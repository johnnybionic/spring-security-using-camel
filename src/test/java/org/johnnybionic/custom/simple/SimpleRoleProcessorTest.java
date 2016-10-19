package org.johnnybionic.custom.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.johnnybionic.CamelSecurityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
public class SimpleRoleProcessorTest {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_EXTRA = "ROLE_EXTRA";

    private static final String ROLES = "roles";

    private static final String NON_ADMIN_USERNAME = "user";
    private static final String NON_ADMIN_PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String EXTRA_USERNAME = "extra";
    private static final String EXTRA_PASSWORD = "extra";

    @Autowired
    private SimpleRoleProcessor processor;

    @Test
    public void testUserRoleAdded() throws Exception {

        Exchange exchange = commonProcess(NON_ADMIN_USERNAME, NON_ADMIN_PASSWORD);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(1, header.size());

        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_USER)));
    }

    @Test
    public void thatAdminRoleAdded() throws Exception {

        Exchange exchange = commonProcess(ADMIN_USERNAME, ADMIN_PASSWORD);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(2, header.size());

        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_USER)));
        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));

    }

    @Test
    public void thatAdminRoleNotAddedForBadPassword() throws Exception {

        Exchange exchange = commonProcess(ADMIN_USERNAME, NON_ADMIN_PASSWORD);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(1, header.size());

        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_USER)));
        assertFalse(header.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));

    }

    @Test
    public void thatThirdRoleIsAdded() throws Exception {

        Exchange exchange = commonProcess(EXTRA_USERNAME, EXTRA_PASSWORD);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(2, header.size());

        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_USER)));
        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_EXTRA)));
        assertFalse(header.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));

    }

    @Test
    public void thatThirdRoleIsNotAddedForBadPassword() throws Exception {

        Exchange exchange = commonProcess(EXTRA_USERNAME, NON_ADMIN_PASSWORD);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(1, header.size());

        assertTrue(header.contains(new SimpleGrantedAuthority(ROLE_USER)));
        assertFalse(header.contains(new SimpleGrantedAuthority(ROLE_EXTRA)));
        assertFalse(header.contains(new SimpleGrantedAuthority(ROLE_ADMIN)));

    }

    private Exchange commonProcess(String username, String password) {
        CamelContext context = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(context);

        List<GrantedAuthority> roles = new ArrayList<>();
        User user = new User(username, password, roles);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
                roles);
        exchange.getIn().setBody(token);

        try {
            processor.process(exchange);
        }
        catch (Exception e) {
            fail();
        }

        return exchange;
    }
}
