/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.johnnybionic.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Simple controller. Based on a Spring sample project.
 *
 * @author johnny
 * @author Joe Grandja
 */
@Controller
public class MainController {

    public static final String LOGIN_ERROR_ATTRIBUTE = "loginError";
    public static final String LOGIN_PAGE = "login";
    public static final String PUBLIC_INDEX_PAGE = "public/index";
    public static final String ADMIN_INDEX_PAGE = "admin/index";
    public static final String USER_INDEX_PAGE = "user/index";
    public static final String EXTRA_INDEX_PAGE = "extra/index";
    public static final String INDEX_PAGE = "index";

    /** root. @return redirect to index */
    @RequestMapping("/")
    public String root() {
        return "redirect:/" + INDEX_PAGE;
    }

    /** main. @return index page */
    @RequestMapping("/index")
    public String index() {
        return INDEX_PAGE;
    }

    /** user page. @return user index page */
    @RequestMapping("/user/index")
    public String userIndex() {
        return USER_INDEX_PAGE;
    }

    /** admin page. @return admin index page */
    @RequestMapping("/admin/index")
    public String adminIndex() {
        return ADMIN_INDEX_PAGE;
    }

    /** public page. @return public index page */
    @RequestMapping("/public/index")
    public String publicIndex() {
        return PUBLIC_INDEX_PAGE;
    }

    @RequestMapping("/extra/index")
    public String extraIndex() {
        return EXTRA_INDEX_PAGE;
    }

    /*
     * Login page. The GET below serves the login page. The POST is handled by
     * Spring Security's filter chain, and is specified in SecurityConfig.
     */
    /** login. @return login page */
    @RequestMapping(value = "/login")
    public String login() {
        return LOGIN_PAGE;
    }

    /**
     * login error.
     *
     * @param model the model
     * @return login error page
     */
    @RequestMapping("/login-error")
    public String loginError(final Model model) {
        model.addAttribute(LOGIN_ERROR_ATTRIBUTE, true);
        return LOGIN_PAGE;
    }

}
