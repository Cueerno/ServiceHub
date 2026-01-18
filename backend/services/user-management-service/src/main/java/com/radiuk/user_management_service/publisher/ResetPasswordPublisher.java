package com.radiuk.user_management_service.publisher;

import com.radiuk.user_management_service.event.ResetPasswordEvent;
import com.radiuk.user_management_service.util.RabbitConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetPasswordPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(ResetPasswordEvent resetPasswordEvent) {
        log.info("Publishing PASSWORD_RESET event for email={}", resetPasswordEvent.email());

        rabbitTemplate.convertAndSend(
                RabbitConstants.NOTIFICATION_EXCHANGE,
                RabbitConstants.PASSWORD_RESET_ROUTING_KEY,
                resetPasswordEvent
        );
    }
}
