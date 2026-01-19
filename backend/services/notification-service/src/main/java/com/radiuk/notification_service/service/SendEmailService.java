package com.radiuk.notification_service.service;

import com.radiuk.notification_service.entity.PasswordResetMessage;
import com.radiuk.notification_service.repository.PasswordResetMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SendEmailService {

    private final PasswordResetMessageRepository repository;

    @Scheduled(fixedDelay = 60000)
    public void sendPasswordResetEmail() {
        List<PasswordResetMessage> unsendMessages = repository.findBySentAtIsNull();

        for (PasswordResetMessage message : unsendMessages) {
            log.info("Sending message to reset password email={}",  message.getEmailAddress());
            message.setSentAt(Instant.now());
        }
    }
}
