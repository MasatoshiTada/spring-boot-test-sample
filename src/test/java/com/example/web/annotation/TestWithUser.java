package com.example.web.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ユーザー権限でのテストメソッドに付加するアノテーションです。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@WithUserDetails(userDetailsServiceBeanName = "accountDetailsService",
        value = "user@example.com")
public @interface TestWithUser {}
