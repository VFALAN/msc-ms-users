package com.msc.ms.users.user.validation.existing.location;

import com.msc.ms.users.location.ILocationService;
import com.msc.ms.users.security.HeaderService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Slf4j
public class ExistingLocationValidator implements ConstraintValidator<ExistingLocation, Integer> {
    private final ILocationService iLocationService;
    @Value("${msc.security.own.key}")
    private String key;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        try {
            log.info("validating location: {}", value);
            final var response = iLocationService.getLocation( value);
            log.info(response.getStatusCode().toString());
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

    }
}
