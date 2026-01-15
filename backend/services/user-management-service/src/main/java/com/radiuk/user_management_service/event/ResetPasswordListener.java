package com.radiuk.user_management_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ResetPasswordListener {

    private final ResetPasswordPublisher resetPasswordPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    private void handleResetPassword(ResetPasswordEvent resetPasswordEvent) {
        resetPasswordPublisher.publish(resetPasswordEvent);
    }
}
