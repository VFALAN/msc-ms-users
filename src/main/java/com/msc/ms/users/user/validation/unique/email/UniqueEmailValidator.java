package com.msc.ms.users.user.validation.unique.email;

import com.msc.ms.users.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private  UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("validating email: {}", value);
        return userService.validEmail(value);
    }
}
