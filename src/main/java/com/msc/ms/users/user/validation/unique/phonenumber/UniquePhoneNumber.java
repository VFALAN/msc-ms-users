package com.msc.ms.users.user.validation.unique.phonenumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniquePhoneNumberValidator.class)
public @interface UniquePhoneNumber {
    String message() default "Phone Number already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
