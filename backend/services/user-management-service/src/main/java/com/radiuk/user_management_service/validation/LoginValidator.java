package com.radiuk.user_management_service.validation;

import com.radiuk.user_management_service.annotation.ValidLogin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

public class LoginValidator implements ConstraintValidator<ValidLogin, String> {

    private final EmailValidator emailValidator = new EmailValidator();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false;
        }

        if (value.contains("@")) {
            return emailValidator.isValid(value, null);
        }

        return value.matches("^[A-Za-z0-9]{3,50}$");
    }
}