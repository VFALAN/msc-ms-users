package com.msc.ms.users.user.validation.existing.location;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ExistingLocationValidator.class)
public @interface ExistingLocation {
    String message() default "Location not exist in the system";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
