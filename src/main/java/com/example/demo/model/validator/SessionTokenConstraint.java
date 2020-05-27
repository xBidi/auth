package com.example.demo.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented @Constraint(validatedBy = SessionTokenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD,ElementType.PARAMETER}) @Retention(value = RetentionPolicy.RUNTIME)
public @interface SessionTokenConstraint {
    String message() default "invalid session token format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
