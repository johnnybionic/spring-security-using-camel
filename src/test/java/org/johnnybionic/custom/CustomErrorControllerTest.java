package org.johnnybionic.custom;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.johnnybionic.CamelSecurityApplication;
import org.johnnybionic.error.CustomErrorController;
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

/**
 * Test handling of errors. This might not be possible due to Spring Boot
 * limitations.
 *
 * @author johnny
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelSecurityApplication.class)
@WebAppConfiguration
public class CustomErrorControllerTest {

    @Autowired
    private CustomErrorController controller;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Not sure if there's any value in testing this.
     */
    @Test
    public void thatErrorPathIsNotNull() {
        assertNotNull(controller.getErrorPath());
    }

    /**
     * Simulate an error. This doesn't invoke the custom error handler.
     *
     * @throws Exception
     */
    @Test
    public void pageNotFound() throws Exception {
        mockMvc.perform(get("/notThere")).andExpect(status().is4xxClientError());

    }

    /**
     * Simulate an error, but directly invoking the /error endpoint
     *
     * @throws Exception
     */
    @Test
    public void errorPage() throws Exception {
        mockMvc.perform(get("/error")).andExpect(status().isOk())
                .andExpect(view().name(CustomErrorController.ERROR_VIEW_NAME));

    }

    /**
     * Simulate an error, but directly invoking the /error endpoint
     *
     * @throws Exception
     */
    @Test
    public void unauthorisedPage() throws Exception {
        mockMvc.perform(get("/403")).andExpect(status().isOk())
                .andExpect(view().name(CustomErrorController.UNAUTHORISED_VIEW_NAME));

    }

    // @Test
    // public void unauthorisedUser() throws Exception {
    // mockMvc.perform(get("/admin/index")).andExpect(status().is4xxClientError())
    // .andExpect(view().name(CustomErrorController.UNAUTHORISED_VIEW_NAME));
    //
    // }
}
