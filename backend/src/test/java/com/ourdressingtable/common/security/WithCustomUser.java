package com.ourdressingtable.common.security;

import com.ourdressingtable.member.domain.Role;
import com.ourdressingtable.member.domain.Status;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomUserSecurityContextFactory.class)
public @interface WithCustomUser {

    long memberId() default 1L;
    String email() default "test@example.com";
    String password() default "password";
    Role role() default Role.ROLE_BASIC;
    Status status() default Status.ACTIVE;

}
