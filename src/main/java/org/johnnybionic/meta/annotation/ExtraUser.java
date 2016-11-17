package org.johnnybionic.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Ensures the current user has the role ROLE_EXTRA. Abstracts the SpEL part,
 * and uses {@link Authorise} to perform the check.
 *
 * @author johnny
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorise.checkUserHasExtraRole(authentication)")
public @interface ExtraUser {

}
