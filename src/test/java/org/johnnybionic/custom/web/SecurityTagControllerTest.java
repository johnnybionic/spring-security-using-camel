package org.johnnybionic.custom.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.johnnybionic.CamelSecurityApplication;
import org.johnnybionic.web.MainController;
import org.johnnybionic.web.SecurityTagController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests the alternate controller, that uses security tags. The URLS are
 * prepended with '/tags', and redirect to MainController (i.e. without the
 * '/tags'.
 *
 * An excellent reference for annotation testing is here:
 * http://www.concretepage.com/spring-4/spring-4-security-junit-test-with-
 * withmockuser-and-withuserdetails-annotation-example-using-webappconfiguration
 *
 * @author johnny
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
public class SecurityTagControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SecurityTagController controller;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Calling this via mockMvc results in the expected exception, however it's
     * wrapped in a NestedServletException
     *
     * @throws Exception
     */
    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void whenAdminPage_withoutRole_thenError() throws Exception {
        controller.adminIndex(null);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "USER")
    public void whenAdminPage_withWrongRole_thenError() {
        controller.adminIndex(null);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void whenAdminPage_withAdminRole_thenSuccess() throws Exception {
        mockMvc.perform(get(SecurityTagController.TAGS_ADMIN_INDEX)).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("admin")
    public void whenAdminPage_withAdminUser_thenSuccess() throws Exception {
        mockMvc.perform(get(SecurityTagController.TAGS_ADMIN_INDEX)).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user")
    public void whenUserPage_withNormalUser_thenSuccess() throws Exception {
        mockMvc.perform(get(SecurityTagController.TAGS_USER_INDEX)).andExpect(status().isOk());
    }

    // the 'extra' page is only accessible by a user with ROLE_EXTRA
    @Test(expected = AccessDeniedException.class)
    @WithUserDetails("user")
    public void whenExtraPage_withNormalUser_thenFailure() throws Exception {
        controller.extraUser(null);
    }

    @Test
    @WithUserDetails("extra")
    public void whenExtraPage_withExtraUser_thenSuccess() throws Exception {
        mockMvc.perform(get(SecurityTagController.TAGS_EXTRA_PAGE)).andExpect(status().isOk());
    }

    @Test
    public void thatLoginPageReturned() throws Exception {
        generalPageTest("/tags/index", MainController.INDEX_PAGE);
    }

    private void generalPageTest(final String url, final String page) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(view().name(page));
    }

    // tests the logging method
    @Test
    public void whenNullUser_thenLogUser_isOK() {
        controller.index(null);
    }
}
