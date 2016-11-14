package org.johnnybionic.web;

import static org.johnnybionic.web.MainController.ADMIN_INDEX_PAGE;
import static org.johnnybionic.web.MainController.INDEX_PAGE;
import static org.johnnybionic.web.MainController.USER_INDEX_PAGE;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller uses Spring security tags to control access.
 *
 * @author johnny
 *
 */
@Controller
public class SecurityTagController {

    public static final String TAGS_USER_INDEX = "/tags/user/index";
    public static final String TAGS_ADMIN_INDEX = "/tags/admin/index";

    /** main. @return index page */
    @RequestMapping("/tags/index")
    public String index() {
        return INDEX_PAGE;
    }

    /** admin page. @return admin index page */
    @RequestMapping(TAGS_ADMIN_INDEX)
    @Secured("ROLE_ADMIN")
    public String adminIndex() {
        return ADMIN_INDEX_PAGE;
    }

    /** user page. @return user index page */
    @RequestMapping(TAGS_USER_INDEX)
    @Secured("ROLE_USER")
    public String userIndex() {
        return USER_INDEX_PAGE;
    }

}
