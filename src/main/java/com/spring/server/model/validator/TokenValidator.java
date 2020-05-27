package com.spring.server.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TokenValidator implements ConstraintValidator<TokenConstraint, String> {

    @Override public void initialize(TokenConstraint token) {
    }

    @Override public boolean isValid(String token, ConstraintValidatorContext cxt) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        String regexJwt = "Bearer [A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$";
        String genericToken =
            "Bearer [0-9]{13}-[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}";
        if (!token.matches(regexJwt) && !token.matches(genericToken)) {
            return false;
        }
        return true;
    }

}
