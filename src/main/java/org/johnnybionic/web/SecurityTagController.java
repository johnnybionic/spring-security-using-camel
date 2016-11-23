package org.johnnybionic.web;

import static org.johnnybionic.web.MainController.ADMIN_INDEX_PAGE;
import static org.johnnybionic.web.MainController.INDEX_PAGE;
import static org.johnnybionic.web.MainController.USER_INDEX_PAGE;

import org.johnnybionic.meta.annotation.CurrentUser;
import org.johnnybionic.meta.annotation.ExtraUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * This controller uses Spring security tags to control access. Note use of the
 * meta-annotation @CurrentUser, which avoids using Spring's annotations
 * directly.
 *
 * @author johnny
 *
 */
@Slf4j
@Controller
public class SecurityTagController {

    public static final String TAGS_USER_INDEX = "/tags/user/index";
    public static final String TAGS_ADMIN_INDEX = "/tags/admin/index";
    public static final String TAGS_EXTRA_PAGE = "/tags/extra/index";

    /**
     * main.
     *
     * @param user the current user, if available
     * @return index page
     */
    @GetMapping("/tags/index")
    public String index(@CurrentUser final User user) {
        logUser(user);
        return INDEX_PAGE;
    }

    /**
     * admin page. @return admin index page
     *
     * @param user the current user
     */
    @GetMapping(TAGS_ADMIN_INDEX)
    @Secured("ROLE_ADMIN")
    public String adminIndex(@CurrentUser final User user) {
        logUser(user);
        return ADMIN_INDEX_PAGE;
    }

    /**
     * user page. @return user index page
     *
     * @param user the current user
     */
    @GetMapping(TAGS_USER_INDEX)
    @Secured("ROLE_USER")
    public String userIndex(@CurrentUser final User user) {
        logUser(user);
        return USER_INDEX_PAGE;
    }

    /**
     * This call is only allowed for a user with ROLE_EXTRA.
     *
     * @ExtraUser is a meta-annotation that checks if the user has the role.
     *            It's equivalent to @PreAuthorize, using SpEL calling a bean
     *            with the {@link Authentication}
     *
     * @param user the current user
     * @return the standard user index page
     */
    @GetMapping(TAGS_EXTRA_PAGE)
    @ExtraUser
    public String extraUser(@CurrentUser final User user) {
        logUser(user);
        return USER_INDEX_PAGE;
    }

    /**
     * Log the current user, if any.
     *
     * @param user the user
     */
    private void logUser(final User user) {
        if (user != null) {
            log.info("Tagged version of URL called for: " + user.getUsername());
        }
    }
}
