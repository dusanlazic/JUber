package com.nwt.juber.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsMatchValidator.class)
@Documented
public @interface PasswordsMatch {
    String message() default "Passwords do not match.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
