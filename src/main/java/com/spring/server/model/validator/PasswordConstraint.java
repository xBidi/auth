package com.spring.server.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented @Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME) public @interface PasswordConstraint {
    String message() default "invalid password format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
