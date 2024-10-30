package com.msc.ms.users.configuration;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {
    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.port}")
    private String rabbitPort;

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        final var connectionFactory = new CachingConnectionFactory("rabbitmq");
//        final var rabbitUrlConnection = "rabbitmq://" + this.rabbitHost + ":" + rabbitPort;
//        connectionFactory.setAddresses(rabbitUrlConnection);
//        connectionFactory.setVirtualHost("/");
//        connectionFactory.setUsername(this.rabbitUsername);
//        connectionFactory.setPassword(this.rabbitPassword);
//        connectionFactory.setRequestedHeartBeat(30);
//        return connectionFactory;
//    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


}
