package com.radiuk.notification_service.listener;

import com.radiuk.notification_service.entity.ResetPasswordMessage;
import com.radiuk.notification_service.event.PasswordResetEvent;
import com.radiuk.notification_service.repository.ResetPasswordMessageRepository;
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

    private final ResetPasswordMessageRepository resetPasswordRepository;

    @RabbitListener(queues = RabbitConstants.PASSWORD_RESET_QUEUE)
    public void handlePasswordReset(PasswordResetEvent event) {
        log.debug("Received Password Reset, email={}", event.email());

        ResetPasswordMessage message = ResetPasswordMessage.builder()
                .userId(event.userId())
                .emailAddress(event.email())
                .subject("Password reset")
                .body(buildBody(event))
                .build();

        resetPasswordRepository.save(message);

        log.info("Password reset email saved, email={}", event.email());
    }

    private String buildBody(PasswordResetEvent event) {
        String link = "https://frontend.app/reset-password?token=" + event.token();

        return String.format("""
                To: %s
                Reset your password:
                %s
                Expires at: %s
                %n""", event.email(), link, event.expiresAt());
    }
}
