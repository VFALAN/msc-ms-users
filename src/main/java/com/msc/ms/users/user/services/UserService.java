package com.msc.ms.users.user.services;

import com.msc.ms.users.address.AddressEntity;
import com.msc.ms.users.authentication.IAuthenticationService;
import com.msc.ms.users.authentication.model.LogPassHistoryRequest;
import com.msc.ms.users.authentication.model.LogPasswordResponse;
import com.msc.ms.users.crypto.CryptoService;
import com.msc.ms.users.location.ILocationService;
import com.msc.ms.users.location.LocationResponse;
import com.msc.ms.users.minio.IMinioService;
import com.msc.ms.users.passlogs.ILogPassRepository;
import com.msc.ms.users.passlogs.LogPassEntity;
import com.msc.ms.users.profile.ProfileService;
import com.msc.ms.users.user.UserRepository;
import com.msc.ms.users.user.model.UserEntity;
import com.msc.ms.users.user.model.request.UserRegistryRequest;
import com.msc.ms.users.user.model.request.UserRequestDTO;
import com.msc.ms.users.user.model.response.UserRegistryResponse;
import com.msc.ms.users.user.model.response.UserResponseDTO;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Timed("users")
public class UserService {

    public UserService(
            final UserRepository pUserRepository,
            final ProfileService pProfileService,
            final ILocationService pILocationService,
            final ModelMapper pModelMapper,
            final IAuthenticationService pIAuthenticationService,
            final ILogPassRepository pILogPassRepository,
            final PasswordEncoder pPasswordEncoder,
            final IMinioService pIMinioService,
            final CryptoService pCryptoService
    ) {
        userRepository = pUserRepository;
        profileService = pProfileService;
        iLocationService = pILocationService;
        modelMapper = pModelMapper;
        iAuthenticationService = pIAuthenticationService;
        iLogPassRepository = pILogPassRepository;
        passwordEncoder = pPasswordEncoder;
        iMinioService = pIMinioService;
        cryptoService = pCryptoService;
    }

    private static final boolean IS_ACTIVE = true;
    private static final String CANDIDATE_STUDENT_KEY = "CS";
    private final UserRepository userRepository;
    private final ProfileService profileService;
    private final ILocationService iLocationService;
    private final ModelMapper modelMapper;
    private final IAuthenticationService iAuthenticationService;
    private final ILogPassRepository iLogPassRepository;
    private final PasswordEncoder passwordEncoder;
    private final IMinioService iMinioService;
    private final CryptoService cryptoService;


    @Counted(value = "user.registry")
    @Timed(value = "user.registry")
    public UserRegistryResponse userRegistry(UserRegistryRequest pRequest) throws Exception {
        final var entity = modelMapper.map(pRequest, UserEntity.class);
        entity.setActive(IS_ACTIVE);
        entity.setDateCreate(new Date());
        final var mCandidateProfileEntity = profileService.findByKey(CANDIDATE_STUDENT_KEY);
        entity.setProfile(mCandidateProfileEntity);
        final var savedEntity = userRepository.save(entity);
        log.info("user registry success with id: {}", savedEntity.getIdUser());
        final var encryptedPassword = cryptoService.encrypt(pRequest.getPassword());
        final var response = modelMapper.map(savedEntity, UserRegistryResponse.class);
        ResponseEntity<LogPasswordResponse> logResponse = iAuthenticationService.getLogPassword(
                LogPassHistoryRequest
                        .builder()
                        .password(encryptedPassword)
                        .idUser(savedEntity.getIdUser())
                        .build()
        );
        if (logResponse.getStatusCode() == HttpStatus.OK && logResponse.getBody() != null) {
            //  final var body = modelMapper.map(logResponse.getBody(), LogPasswordResponse.class);
            final var body = logResponse.getBody();
            response.setExpirationDate(body.getExpirationDate());
            return response;
        } else {
            log.error("Error during the a call to ms-auth to get the password log history for user: {}", entity.getIdUser());
            throw new Exception("Password Log history creation error");
        }

    }


    //! TODO remove unefficent method

    /// @deprecated
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
        usingDefaultImageForUser(user.getIdUser());
        return modelMapper.map(user, UserResponseDTO.class);


    }

    //!TODO remove function no needed at this point

    @Timed("user.creation.image.linked")
    private void usingDefaultImageForUser(Integer idUser) {
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

        return !(totalUsers > 0);
    }

    public boolean validEmail(String pEmailStr) {
        final var emailUsers = userRepository.findAllByEmailAndActive(pEmailStr, IS_ACTIVE);
        return emailUsers.isEmpty();
    }

    public boolean validPhoneNumber(String pPhoneNumber) {
        return userRepository.findAllByPhoneNumberAndActive(pPhoneNumber, IS_ACTIVE).isEmpty();
    }
}
