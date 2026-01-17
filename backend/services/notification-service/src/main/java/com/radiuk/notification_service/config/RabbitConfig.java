package com.radiuk.notification_service.config;

import com.radiuk.notification_service.util.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(
                RabbitConstants.NOTIFICATION_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue passwordResetQueue() {
        return QueueBuilder.durable(
                RabbitConstants.PASSWORD_RESET_QUEUE
        ).build();
    }

    @Bean
    public Binding passwordResetBinding(
            Queue passwordResetQueue,
            TopicExchange notificationExchange
    ) {
        return BindingBuilder
                .bind(passwordResetQueue)
                .to(notificationExchange)
                .with(RabbitConstants.NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
