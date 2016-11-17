package org.johnnybionic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Configuration of Spring Security.
 *
 * @author johnny
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("camelAuthenticationProvider")
    private AuthenticationProvider authProvider;

    /**
     * Configuration. Note: the 'hasRole' methods take the role name without
     * "ROLE_", whereas the @Secured annotations require it.
     */
    @Override
    //@formatter:off
	protected void configure(final HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                // use mvcMatchers where possible (don't work here though)
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                // for Angular
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
            .formLogin()
                .loginPage("/login").failureUrl("/login-error");
    }
    //@formatter:on

    /**
     * Sets the {@link AuthenticationProvider} used by Spring to our custom
     * provider.
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

}