package com.radiuk.user_management_service.annotation;

import com.radiuk.user_management_service.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be 8–100 characters, contain upper/lowercase letters, a digit, and must not contain '+' or '@'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}