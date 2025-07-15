package com.nhnacademy.bookstoreuserapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BIRTHDAY_EXCHANGE_NAME = "birthday-exchange";
    public static final String BIRTHDAY_QUEUE_NAME = "birthday-queue";
    public static final String BIRTHDAY_ROUTING_KEY = "birthday.user";

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange birthdayExchange() {
        return new DirectExchange(BIRTHDAY_EXCHANGE_NAME);
    }


    @Bean
    public Queue birthdayQueue() {
        return new Queue(BIRTHDAY_QUEUE_NAME, true);
    }

    @Bean
    public Binding birthdayBinding(Queue birthdayQueue, DirectExchange birthdayExchange) {
        return BindingBuilder.bind(birthdayQueue).to(birthdayExchange).with(BIRTHDAY_ROUTING_KEY);
    }
}