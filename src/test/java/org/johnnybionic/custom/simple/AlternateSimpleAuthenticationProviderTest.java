package org.johnnybionic.custom.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Tests the alternative provider. Note that this test does not load the
 * context, as it would require re-loading with a different profile.
 *
 * @author johnny
 *
 */

public class AlternateSimpleAuthenticationProviderTest {

    private static final String USERNAME = "second";
    private static final String PASSWORD = "second";
    private static final String BAD_USERNAME = "wrong";

    private AlternateSimpleAuthenticationProvider provider;

    @Before
    public void setUp() {
        provider = new AlternateSimpleAuthenticationProvider();
    }

    @Test
    public void happyPath() {
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
