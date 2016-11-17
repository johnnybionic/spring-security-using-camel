package org.johnnybionic.meta.annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.johnnybionic.CamelSecurityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Tests the {@link Authorise} component, which provides methods for security
 * annotations using SpEL.
 *
 * @author johnny
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
public class AuthoriseTest {

    @Autowired
    private Authorise authorise;

    @Test
    public void whenNoUser_thenExtraRole_resultFalse() {
        assertFalse(authorise.checkUserHasExtraRole(null));
    }

    @Test
    public void whenUserHasUserRole_thenExtraRole_resultFalse() {
        Authentication authentication = new TestingAuthenticationToken("user", "pw", "ROLE_USER");
        assertFalse(authorise.checkUserHasExtraRole(authentication));
    }

    @Test
    public void whenUserHasExtraRole_thenExtraRole_resultTrue() {
        Authentication authentication = new TestingAuthenticationToken("user", "pw", "ROLE_EXTRA");
        assertTrue(authorise.checkUserHasExtraRole(authentication));
    }
}
