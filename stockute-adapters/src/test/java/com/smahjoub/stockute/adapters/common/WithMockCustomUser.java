package com.smahjoub.stockute.adapters.common;

import org.springframework.security.test.context.support.WithSecurityContext;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String username() default "admin";

    String[] roles() default {"USER", "ADMIN"};
}
