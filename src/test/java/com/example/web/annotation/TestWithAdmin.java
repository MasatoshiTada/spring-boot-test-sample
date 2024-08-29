package com.example.web.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理者権限でのテストメソッドに付加するアノテーションです。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@WithUserDetails(userDetailsServiceBeanName = "accountDetailsService",
        value = "admin@example.com")
public @interface TestWithAdmin {}
