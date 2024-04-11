package com.msc.ms.users.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class QueueConfiguration {
    @Value("#{'${mac.queues}'.split(',')}")
    private List<String> queues;
    private final RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void postConstruct() {
        this.queues.forEach(queue -> {
            if (rabbitAdmin.getQueueProperties(queue) != null) {
                log.info("Queue {} already exists", queue);
            } else {
                log.info("The queue {} is empty, creating", queue);
                rabbitAdmin.declareQueue(new Queue(queue));
                log.info("Queue {} created", queue);
            }
        });
    }
}
