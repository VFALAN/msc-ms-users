package com.msc.ms.users.message.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    private final RabbitTemplate rabbitTemplate;

    UserProducer(final RabbitTemplate pRabbitTemplate) {
        rabbitTemplate = pRabbitTemplate;
    }
}
