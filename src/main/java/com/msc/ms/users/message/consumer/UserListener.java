package com.msc.ms.users.message.consumer;

import com.msc.ms.users.user.model.UserRequestDTO;
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

    @RabbitListener(queues = "${queue.user}")
    public void processUser(UserRequestDTO message) {
        log.info(message.getUserName());
        Errors errors = new BeanPropertyBindingResult(message, "UserMessage");
        ValidationUtils.invokeValidator(validator, message, errors);
        if (errors.hasErrors()) {
           // todo add the logic for errors in queue
        }
    }
}
