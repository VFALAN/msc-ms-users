package com.msc.ms.users.user.validation.unique.phonenumber;

import com.msc.ms.users.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniquePhoneNumberValidator implements ConstraintValidator<UniquePhoneNumber, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userService.validPhoneNumber(value);
    }
}
