package com.msc.ms.users.user;

import com.msc.ms.users.address.AddressEntity;
import com.msc.ms.users.address.AddressService;
import com.msc.ms.users.authentication.IAuthenticationService;
import com.msc.ms.users.location.ILocationService;
import com.msc.ms.users.location.LocationResponse;
import com.msc.ms.users.minio.IMinioService;
import com.msc.ms.users.passlogs.ILogPassRepository;
import com.msc.ms.users.passlogs.LogPassEntity;
import com.msc.ms.users.profile.ProfileService;
import com.msc.ms.users.user.model.UserEntity;
import com.msc.ms.users.user.model.UserRequestDTO;
import com.msc.ms.users.user.model.UserResponseDTO;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Timed("users")
public class UserService {
    private static final boolean IS_ACTIVE = true;
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final ProfileService profileService;
    private final ILocationService iLocationService;
    private final ModelMapper modelMapper;
    private final IAuthenticationService iAuthenticationService;
    private final ILogPassRepository iLogPassRepository;
    private final PasswordEncoder passwordEncoder;
    private final IMinioService iMinioService;
    @Value("${msc.security.own.key}")
    private String KEY;

    @Counted(value = "user.created", description = "Creation of a new User")
    @Timed(value = "user.created", description = "time taken for the user creation")
    public UserResponseDTO createUser(UserRequestDTO pUserRequestDTO) throws Exception {


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
        asingDefaultImageForUser(user.getIdUser());
        return modelMapper.map(user, UserResponseDTO.class);


    }

    @Timed("user.creation.image.linked")
    private void asingDefaultImageForUser(Integer idUser) {
        final var response = iMinioService.defaultImage(idUser);
        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("default profile image linked at user: {}", idUser);
        } else {
            log.error("something went wrong during the process for the user {}", idUser);
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


    public List<UserResponseDTO> list() {
        return this.userRepository.findAll().stream().map(i -> this.modelMapper.map(i, UserResponseDTO.class)).toList();
    }

    public boolean validUsername(String username) {
        final var totalUsers = userRepository.searchUsername(username);
        return totalUsers == 0;
    }

    public boolean validEmail(String pEmailStr) {
        final var emailUsers = userRepository.findAllByEmailAndActive(pEmailStr, IS_ACTIVE);
        return emailUsers.isEmpty();
    }

    public boolean validPhoneNumber(String pPhoneNumber) {
        return userRepository.findAllByPhoneNumberAndActive(pPhoneNumber, IS_ACTIVE).isEmpty();
    }
}
