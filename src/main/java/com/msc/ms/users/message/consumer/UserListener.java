package com.msc.ms.users.message.consumer;

import com.msc.ms.users.user.UserService;
import com.msc.ms.users.user.model.UserRequestDTO;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserListener {
    private final Validator validator;
    private final UserService userService;

    @RabbitListener(queues = "${queue.user}")
    @Counted(value = "users.queue.message.get")
    public void processUser(UserRequestDTO message) throws Exception {
        log.info(message.getUserName());
        Errors errors = new BeanPropertyBindingResult(message, "UserMessage");
        ValidationUtils.invokeValidator(validator, message, errors);
        if (errors.hasErrors()) {
            this.registryError(errors, message);
        } else {
            try {
                userService.createUser(message);
            } catch (Exception e) {
                log.error("error in process {}", e.getMessage());
            }
        }
    }

    @Counted(value = "users.queue.message.errors")
    private void registryError(Errors errors, UserRequestDTO userRequestDTO) {
        log.error("username: {} con not be added due", userRequestDTO.getUserName());
        errors.getAllErrors().forEach(e -> {
            final var message = e.getDefaultMessage() != null ? e.getDefaultMessage() : "";
            switch (message) {
                case "Location not exist in the system" -> registryExistingLocation(userRequestDTO, e);
                case "Profile not exist in the system" -> registryExistingProfile(userRequestDTO, e);
                case "Email already exists" -> registryUniqueEmailError(userRequestDTO, e);
                case "Phone Number already exists" -> registryPhoneNumberEmailError(userRequestDTO,e);
                case "Username already exists" ->registryUsernameEmailError(userRequestDTO,e);
                default -> registryCommonError(userRequestDTO, e);
            }
        });
    }


    @Counted(value = "users.queue.message.error.username")
    private void registryUsernameEmailError(UserRequestDTO userRequestDTO, ObjectError e) {
        log.info("Username Not Valid Or is Already Existing  username: {} message: {}", userRequestDTO.getUserName(), e.getDefaultMessage());
    }

    @Counted(value = "users.queue.message.error.phonenumber")
    private void registryPhoneNumberEmailError(UserRequestDTO userRequestDTO, ObjectError e) {
        log.info("Phone Number Not Valid Or is Already Existing  Phone Number: {} message: {}", userRequestDTO.getPhoneNumber(), e.getDefaultMessage());
    }

    @Counted(value = "users.queue.message.error.email")
    private void registryUniqueEmailError(UserRequestDTO userRequestDTO, ObjectError e) {
        log.info("Email Not Valid Or is Already Existing  email: {} message: {}", userRequestDTO.getEmail(), e.getDefaultMessage());
    }


    @Counted(value = "users.queue.message.error.location")
    private void registryExistingLocation(UserRequestDTO userRequestDTO, ObjectError e) {
        log.info("Location Not Exist Or is Enabled Location id:{} message: {}", userRequestDTO.getIdLocation(), e.getDefaultMessage());
    }

    @Counted(value = "users.queue.message.error.profile")
    private void registryExistingProfile(UserRequestDTO userRequestDTO, ObjectError e) {
        log.info("Profile Not Exist Or is Enabled Profile id:{} message: {}", userRequestDTO.getIdProfile(),e.getDefaultMessage());
    }


    @Counted(value = "users.queue.message.error.common")
    private void registryCommonError(UserRequestDTO userRequestDTO, ObjectError e) {
        log.info("Common Error in validation for users in queue processing for email: {} message: {}", userRequestDTO.getEmail(), e.getDefaultMessage());
    }
}
