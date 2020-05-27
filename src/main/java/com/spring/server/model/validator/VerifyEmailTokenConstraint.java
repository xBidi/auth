package com.spring.server.model.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented @Constraint(validatedBy = VerifyEmailTokenValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME) public @interface VerifyEmailTokenConstraint {
    String message() default "invalid verify email token format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
