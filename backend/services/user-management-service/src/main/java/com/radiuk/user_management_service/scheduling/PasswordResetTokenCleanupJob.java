package com.radiuk.user_management_service.scheduling;

import com.radiuk.user_management_service.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetTokenCleanupJob {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        int deleted = passwordResetTokenRepository.deleteExpiredOrUsed(now);

        if (deleted > 0) {
            log.info("Password reset tokens cleanup: {} tokens removed", deleted);
        }

        System.out.println("scheduling password reset token cleanup job");
    }
}
