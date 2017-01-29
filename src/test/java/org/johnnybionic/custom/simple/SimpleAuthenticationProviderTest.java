package org.johnnybionic.custom.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.johnnybionic.CamelSecurityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
public class SimpleAuthenticationProviderTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String BAD_USERNAME = "error";
    private static final String BAD_PASSWORD = "error";

    @Autowired
    private SimpleAuthenticationProvider provider;

    @Test
    public void testHappyPath() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        Authentication authenticate = provider.authenticate(token);
        assertTrue(authenticate == token);
    }

    @Test(expected = org.springframework.security.core.AuthenticationException.class)
    public void testBadUsername() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(BAD_USERNAME, PASSWORD);
        provider.authenticate(token);
        fail();
    }

    @Test(expected = org.springframework.security.core.AuthenticationException.class)
    public void testBadPassword() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(USERNAME, BAD_PASSWORD);
        provider.authenticate(token);
        fail();
    }

    /**
     * Ensure this method is not changed inadvertently.
     */
    @Test
    public void whenSupportsWithCorrectClassThenTrue() {
        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void whenSupportsWithIncorrectClassThenFalse() {
        assertFalse((provider.supports(String.class)));
    }

}
