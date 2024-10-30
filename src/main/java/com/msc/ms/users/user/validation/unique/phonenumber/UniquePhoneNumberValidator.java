package com.msc.ms.users.user.validation.unique.phonenumber;

import com.msc.ms.users.user.services.UserService;
import com.msc.ms.users.user.validation.unique.email.UniqueEmailValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {
    private final UserService userService;

    public UniquePhoneNumberValidator(final UserService pUserService) {
        userService = pUserService;
    }

    @Override
    public void initialize(UniquePhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userService.validPhoneNumber(value);
    }
}
