package com.radiuk.notification_service.service;

import com.radiuk.notification_service.entity.EmailStatus;
import com.radiuk.notification_service.entity.PasswordResetMessage;
import com.radiuk.notification_service.repository.PasswordResetMessageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;
    private final PasswordResetMessageRepository repository;

    private static final String SUBJECT = "Reset Password";
    private static final String FRONTEND_URL = "http://localhost:3000/";

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void sendPasswordResetEmail() {
        List<PasswordResetMessage> unsendMessages = repository.findBySentAtIsNull();

        for (PasswordResetMessage message : unsendMessages) {
            message.setStatus(EmailStatus.PENDING);
            try {
                sendEmail(message);
                message.setSentAt(Instant.now());
                message.setStatus(EmailStatus.SENT);
                log.info("Email successfully sent to {}", message.getEmailAddress());
            } catch (Exception exception) {
                message.setStatus(EmailStatus.FAILED);
                log.error("Failed to send email to {}", message.getEmailAddress(), exception);
            }
        }
    }

    private void sendEmail(PasswordResetMessage message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(mimeMessage, false, "UTF-8");

        helper.setTo(message.getEmailAddress());
        helper.setSubject(SUBJECT);

        String resetLink = FRONTEND_URL + "reset?token=" + message.getToken();

        String html = """
            Dear user,<br><br>
            Click the link below to reset your password:<br><br>
            %s - Reset Password<br><br>
            If you did not request this, ignore this email.
            """.formatted(resetLink);

        helper.setText(html, true);

        mailSender.send(mimeMessage);
    }
}
