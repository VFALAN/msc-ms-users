package com.msc.ms.users.user.validation.format.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordPatternValidator implements ConstraintValidator<PasswordPattern, String> {
    @Override
    public void initialize(PasswordPattern constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        final var regex = "(?=.*[#|!|@|#|\\$|\\^|&|\\*])[^,.\\/\\[\\]\\{\\}\\|\\?;><:+]{8,10}";
        
        return value.matches(regex);
    }
}
