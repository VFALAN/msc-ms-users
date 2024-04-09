package com.msc.ms.users.user.validation.existing.profile;

import com.msc.ms.users.profile.ProfileService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class ExistingProfileValidator implements ConstraintValidator<ExistingProfile, Integer> {
    private final ProfileService profileService;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        var isValid = false;
        try {
            final var profile = profileService.findById(value);
            isValid = profile != null;
        } catch (Exception e) {
            log.error("Error validating existing profile: {}", e.getMessage());
        }
        return isValid;
    }
}
