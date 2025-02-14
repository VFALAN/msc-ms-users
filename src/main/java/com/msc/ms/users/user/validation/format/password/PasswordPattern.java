package com.msc.ms.users.user.validation.format.password;

import com.msc.ms.users.user.validation.unique.email.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordPatternValidator.class)
public @interface PasswordPattern {
    String message() default "Password Pattern is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
