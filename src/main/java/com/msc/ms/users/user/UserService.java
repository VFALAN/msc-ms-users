package com.msc.ms.users.user;

import com.msc.ms.users.address.AddressEntity;
import com.msc.ms.users.address.AddressService;
import com.msc.ms.users.authentication.IAuthenticationService;
import com.msc.ms.users.location.ILocationService;
import com.msc.ms.users.location.LocationResponse;
import com.msc.ms.users.passlogs.ILogPassRepository;
import com.msc.ms.users.passlogs.LogPassEntity;
import com.msc.ms.users.profile.ProfileService;
import com.msc.ms.users.user.error.AlreadyExistingUsernameException;
import com.msc.ms.users.user.model.UserEntity;
import com.msc.ms.users.user.model.UserRequestDTO;
import com.msc.ms.users.user.model.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final ProfileService profileService;
    private final ILocationService iLocationService;
    private final ModelMapper modelMapper;
    private final IAuthenticationService iAuthenticationService;
    private final ILogPassRepository iLogPassRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserRequestDTO pUserRequestDTO) throws Exception {
        if (this.validUsername(pUserRequestDTO.getUserName())) {
            final var mLocation = this.getLocation(pUserRequestDTO.getIdLocation());
            final var mProfileentity = profileService.findById(pUserRequestDTO.getIdProfile());
            final var address = modelMapper.map(pUserRequestDTO, AddressEntity.class);
            address.setIdLocation(mLocation.getIdLocality());
            var user = modelMapper.map(pUserRequestDTO, UserEntity.class);
            user.setAddress(address);
            user.setProfile(mProfileentity);
            user.setActive(true);
            user.setDateCreate(new Date());
            user = this.userRepository.save(user);
            log.info("user created with id: {}", user.getIdUser());
            final var password = iAuthenticationService.getPassword(10);
            final var mPassLog = LogPassEntity.builder()
                    .idUser(user)
                    .password(passwordEncoder.encode(password.getBody()))
                    .build();
            iLogPassRepository.save(mPassLog);
            log.info("with Password for {} : {} ", user.getUserName(), password.getBody());
            return modelMapper.map(user, UserResponseDTO.class);
        } else {
            throw new AlreadyExistingUsernameException(pUserRequestDTO.getUserName());
        }

    }


    private LocationResponse getLocation(Integer idLocation) throws Exception {
        final var response = iLocationService.getLocation(idLocation);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.info("response of search of location fot the location with id: {} got the net status: {}", idLocation, response.getStatusCode().value());
            throw new Exception("No existing record in the system");
        }
    }

    private boolean validUsername(String username) {
        final var totalUsers = userRepository.searchUsername(username);
        return totalUsers == 0;
    }

    private static Date estimatePasswordExpiration(Date date) {
        final var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }
}
