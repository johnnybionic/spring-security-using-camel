/*
 * Copyright 2012-2016 the original author or authors.
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
package org.johnnybionic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

/**
 * @author johnny developed from a Spring Security example by
 * @author Joe Grandja
 */
@SpringBootApplication
public class CamelSecurityApplication {

    /**
     * Main entry point.
     *
     * @param args command-line
     */
    public static void main(final String[] args) {
        SpringApplication.run(CamelSecurityApplication.class, args);
    }

    /**
     * Adds custom error handling for 403 errors.
     *
     * @return {@link EmbeddedServletContainerCustomizer}
     */
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
        };
    }
}
