package org.johnnybionic.custom.simple;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.hamcrest.Matcher;
import org.johnnybionic.CamelSecurityApplication;
import org.johnnybionic.custom.custom.CustomMatchers;
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
@SuppressWarnings("unchecked")
public class SimpleRoleProcessorTest {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_EXTRA = "ROLE_EXTRA";
    private static final SimpleGrantedAuthority GRANTED_AUTHORITY_EXTRA = new SimpleGrantedAuthority(ROLE_EXTRA);
    private static final String ROLE_EXISTING = "ROLE_EXISTING";

    private static final String ROLES = "roles";

    private static final String NON_ADMIN_USERNAME = "user";
    private static final String NON_ADMIN_PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String EXTRA_USERNAME = "extra";
    private static final String EXTRA_PASSWORD = "extra";

    private static final SimpleGrantedAuthority GRANTED_AUTHORITY_USER = new SimpleGrantedAuthority(ROLE_USER);
    private static final SimpleGrantedAuthority GRANTED_AUTHORITY_ADMIN = new SimpleGrantedAuthority(ROLE_ADMIN);

    @Autowired
    private SimpleRoleProcessor processor;

    @Test
    public void testUserRoleAdded() throws Exception {

        Exchange exchange = commonProcess(NON_ADMIN_USERNAME, NON_ADMIN_PASSWORD, null);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(1, header.size());

        assertThat("Expected USER role", header, contains(GRANTED_AUTHORITY_USER));
    }

    @Test
    public void thatAdminRoleAdded() throws Exception {

        Exchange exchange = commonProcess(ADMIN_USERNAME, ADMIN_PASSWORD, null);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(2, header.size());

        assertThat("Expected USER role", header, containsInAnyOrder(GRANTED_AUTHORITY_USER, GRANTED_AUTHORITY_ADMIN));
    }

    @Test
    public void thatAdminRoleNotAddedForBadPassword() throws Exception {

        Exchange exchange = commonProcess(ADMIN_USERNAME, NON_ADMIN_PASSWORD, null);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(1, header.size());

        assertThat("Expected USER role", header, contains(GRANTED_AUTHORITY_USER));
        assertThat("Expected ADMIN role", header, not(contains(GRANTED_AUTHORITY_ADMIN)));

    }

    @Test
    public void thatThirdRoleIsAdded() throws Exception {

        Exchange exchange = commonProcess(EXTRA_USERNAME, EXTRA_PASSWORD, null);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(2, header.size());

        assertThat("Expected USER role", header, containsInAnyOrder(GRANTED_AUTHORITY_USER, GRANTED_AUTHORITY_EXTRA));
        assertThat("Expected ADMIN role", header, not(contains(GRANTED_AUTHORITY_ADMIN)));
    }

    @Test
    public void thatThirdRoleIsNotAddedForBadPassword() throws Exception {

        Exchange exchange = commonProcess(EXTRA_USERNAME, NON_ADMIN_PASSWORD, null);

        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);
        assertNotNull(header);
        assertEquals(1, header.size());

        assertThat("Expected USER role", header, contains(GRANTED_AUTHORITY_USER));
        assertThat("Expected EXTRA role", header, not(contains(GRANTED_AUTHORITY_EXTRA)));
        assertThat("Expected ADMIN role", header, not(contains(GRANTED_AUTHORITY_ADMIN)));

    }

    /**
     * Ensures a role added by a previous processor is still there.
     */
    @Test
    public void whenRoleAlreadyPresent_thenStillExists() {
        List<GrantedAuthority> roles = new ArrayList<>();
        SimpleGrantedAuthority existingRole = new SimpleGrantedAuthority(ROLE_EXISTING);
        roles.add(existingRole);

        Exchange exchange = commonProcess(NON_ADMIN_USERNAME, NON_ADMIN_PASSWORD, roles);
        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);

        assertThat("Expected existing role", header, hasItem(existingRole));
    }

    /**
     * If a previous process added a role, ensure it's not also added by this
     * processor.
     */
    @Test
    public void whenUserRoleAlreadyPresent_thenNotAddedTwice() {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(GRANTED_AUTHORITY_USER);

        Exchange exchange = commonProcess(NON_ADMIN_USERNAME, NON_ADMIN_PASSWORD, roles);
        Collection<GrantedAuthority> header = exchange.getIn().getHeader(ROLES, Collection.class);

        // can Hamcrest do something like this?
        // - although the simplest test would be to ensure the count is one in
        // the first place ...
        assertEquals(1, header.stream().filter(auth -> auth.equals(GRANTED_AUTHORITY_USER)).count());
        assertEquals(1, header.size()); // KISS :)

        // a Matcher that iterates over GrantedAuthority and supertypes to find
        // exactly one
        Matcher<Iterable<? super GrantedAuthority>> exactlyOnce = CustomMatchers
                .exactlyOnce(equalTo(GRANTED_AUTHORITY_USER));

        assertThat("Expected one ROLE_USER", header, exactlyOnce);
    }

    /**
     * Common process for tests.
     *
     * @param username the username
     * @param password the password
     * @param rolesToAdd roles to add, or null if none
     * @return the Exchange returned from the unit
     */
    private Exchange commonProcess(String username, String password, List<GrantedAuthority> rolesToAdd) {
        CamelContext context = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(context);

        List<GrantedAuthority> roles = new ArrayList<>();
        if (rolesToAdd != null) {
            roles.addAll(rolesToAdd);
        }
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
