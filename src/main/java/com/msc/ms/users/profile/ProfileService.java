package com.msc.ms.users.profile;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final IProfileRepository iProfileRepository;


    public ProfileEntity findByKey(@NotNull String pProfileKey) {
        final var mOptionalProfileEntity = iProfileRepository.findByKey(pProfileKey);
        if (mOptionalProfileEntity.isPresent()) {
            return mOptionalProfileEntity.get();
        } else {
            log.error("Profile whit key name: {} not found", pProfileKey);
            throw new NoSuchElementException("No Entity found");
        }

    }

    public ProfileEntity findById(Integer pIdProfile) throws Exception {
        final var mOptionalProfileEntity = iProfileRepository.findById(pIdProfile);
        if (mOptionalProfileEntity.isPresent()) {
            return mOptionalProfileEntity.get();
        } else {
            log.error("profile with id: {} not found", pIdProfile);
            throw new NoSuchElementException("No entity found");
        }
    }

}
