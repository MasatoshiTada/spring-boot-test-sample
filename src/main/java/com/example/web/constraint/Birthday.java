package com.example.web.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 誕生日のバリデーションを行うアノテーションです。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@NotNull
@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
public @interface Birthday {
    String message() default "{com.example.web.constraint.Birthday.message}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Birthday[] value();
    }
}
