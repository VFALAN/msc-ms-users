package com.msc.ms.users.user.validation.existing.location;

import com.msc.ms.users.location.ILocationService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Slf4j
public class ExistingLocationValidator implements ConstraintValidator<ExistingLocation, Integer> {
    private final ILocationService iLocationService;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        try {
            final var response = iLocationService.getLocation(value);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }
}
