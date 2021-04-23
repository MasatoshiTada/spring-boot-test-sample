package com.example.web.web.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@WithAnonymousUser
public @interface TestWithAnonymous {}