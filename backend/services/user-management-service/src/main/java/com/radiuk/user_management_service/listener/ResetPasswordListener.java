package com.radiuk.user_management_service.listener;

import com.radiuk.user_management_service.event.ResetPasswordEvent;
import com.radiuk.user_management_service.publisher.ResetPasswordPublisher;
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
