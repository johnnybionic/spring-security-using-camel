package org.johnnybionic.custom.config;

import java.util.Arrays;

import org.johnnybionic.custom.web.SecurityTagControllerTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Creates a test user for unit tests in {@link SecurityTagControllerTest}
 *
 * @author johnny
 *
 */
@Configuration
public class UserConfiguration {

    @Bean
    public UserDetailsService userDetails() {
        return username -> {

            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + username.toUpperCase());
            UserDetails userDetails = new User(username, username + "123", Arrays.asList(authority));
            return userDetails;
        };
    }

}
