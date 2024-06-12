package com.msc.ms.users.message.consumer;

import com.msc.ms.users.user.UserService;
import com.msc.ms.users.user.model.UserRequestDTO;
import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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
        errors.getAllErrors().forEach(e -> log.info(e.getDefaultMessage()));
    }
}
