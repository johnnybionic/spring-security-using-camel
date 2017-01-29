package org.johnnybionic.custom.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.johnnybionic.CamelSecurityApplication;
import org.johnnybionic.web.MainController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
// @ContextConfiguration(classes = {CamelSecurityApplication.class})
public class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * '/' is redirected to '/index'.
     *
     * @throws Exception
     */
    @Test
    public void testRedirectOfRoot() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/" + MainController.INDEX_PAGE));
    }

    /**
     * Login errors get returned to the login page. An error attribute is added.
     *
     * @throws Exception
     */
    @Test
    public void whenAuthErrorThenRedirectToLoginAndShowError() throws Exception {
        mockMvc.perform(get("/login-error")).andExpect(status().isOk())
                .andExpect(model().attribute(MainController.LOGIN_ERROR_ATTRIBUTE, true))
                .andExpect(view().name(MainController.LOGIN_PAGE));
    }

    /*
     * Not sure how much value these tests add, but they give coverage.
     */
    @Test
    public void thatLoginPageReturned() throws Exception {
        generalPageTest("/login", MainController.LOGIN_PAGE);
    }

    @Test
    public void thatIndexPageReturned() throws Exception {
        generalPageTest("/index", MainController.INDEX_PAGE);
    }

    @Test
    public void thatUserIndexPageReturned() throws Exception {
        generalPageTest("/user/index", MainController.USER_INDEX_PAGE);
    }

    @Test
    public void thatAdminIndexPageReturned() throws Exception {
        generalPageTest("/admin/index", MainController.ADMIN_INDEX_PAGE);
    }

    @Test
    public void thatPublicIndexPageReturned() throws Exception {
        generalPageTest("/public/index", MainController.PUBLIC_INDEX_PAGE);
    }

    @Test
    public void thatExtraIndexPageReturned() throws Exception {
        generalPageTest("/extra/index", MainController.EXTRA_INDEX_PAGE);
    }

    private void generalPageTest(final String url, final String page) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(view().name(page));
    }
}
