package com.radiuk.notification_service.listener;

import com.radiuk.notification_service.entity.EmailStatus;
import com.radiuk.notification_service.entity.PasswordResetMessage;
import com.radiuk.notification_service.event.PasswordResetEvent;
import com.radiuk.notification_service.repository.PasswordResetMessageRepository;
import com.radiuk.notification_service.util.RabbitConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class PasswordResetListener {

    private final PasswordResetMessageRepository resetPasswordRepository;

    @RabbitListener(queues = RabbitConstants.PASSWORD_RESET_QUEUE)
    public void handlePasswordReset(PasswordResetEvent event) {
        log.debug("Received Password Reset, email={}", event.email());

        PasswordResetMessage message = PasswordResetMessage.builder()
                .userId(event.userId())
                .emailAddress(event.email())
                .status(EmailStatus.PENDING)
                .token(event.token())
                .build();

        resetPasswordRepository.save(message);

        log.info("Password reset email saved, email={}", event.email());
    }
}
