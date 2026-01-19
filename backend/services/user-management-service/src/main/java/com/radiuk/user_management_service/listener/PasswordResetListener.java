package com.radiuk.user_management_service.listener;

import com.radiuk.user_management_service.event.PasswordResetEvent;
import com.radiuk.user_management_service.publisher.PasswordResetPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PasswordResetListener {

    private final PasswordResetPublisher passwordResetPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    private void handleResetPassword(PasswordResetEvent passwordResetEvent) {
        passwordResetPublisher.publish(passwordResetEvent);
    }
}
