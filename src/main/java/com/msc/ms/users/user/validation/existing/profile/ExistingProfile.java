package com.msc.ms.users.user.validation.existing.profile;

import com.msc.ms.users.user.validation.existing.location.ExistingLocationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ExistingProfileValidator.class)
public @interface ExistingProfile {
    String message() default "Profile not exist in the system";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
