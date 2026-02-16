package com.radiuk.user_management_service.annotation;

import com.radiuk.user_management_service.validation.LoginValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLogin {

    String message() default "Login must be a valid email or username (3–50 characters, letters and digits only)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}