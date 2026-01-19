package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.dto.PasswordResetConfirmDto;
import com.radiuk.user_management_service.dto.PasswordResetRequestDto;
import com.radiuk.user_management_service.entity.PasswordResetToken;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.event.PasswordResetEvent;
import com.radiuk.user_management_service.exception.ExpiredPasswordResetTokenException;
import com.radiuk.user_management_service.exception.InvalidPasswordResetTokenException;
import com.radiuk.user_management_service.repository.PasswordResetTokenRepository;
import com.radiuk.user_management_service.repository.UserRepository;
import com.radiuk.user_management_service.service.PasswordResetService;
import com.radiuk.user_management_service.util.ResetTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ResetTokenGenerator resetTokenGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${security.password-reset.token-ttl}")
    private Duration passwordResetTokenTtl;

    @Override
    public void resetPasswordRequest(PasswordResetRequestDto dto) {
        userRepository.findByEmail(dto.email()).ifPresent(user -> {
            String token = resetTokenGenerator.generate();

            PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiresAt(Instant.now().plus(passwordResetTokenTtl))
                    .used(false)
                    .build();

            passwordResetTokenRepository.save(passwordResetToken);

            applicationEventPublisher.publishEvent(
                    new PasswordResetEvent(
                            user.getId(),
                            user.getEmail(),
                            token,
                            passwordResetToken.getExpiresAt()
                    )
            );

            log.info("Password reset requested for user with email={}", user.getEmail());
        });
    }

    @Override
    public void resetPasswordConfirm(PasswordResetConfirmDto dto) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(dto.token())
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new InvalidPasswordResetTokenException("Token already used");
        }

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new ExpiredPasswordResetTokenException("Token expired");
        }

        User user = userRepository.findById(resetToken.getUser().getId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        resetToken.setUsed(true);

        log.info("Password reset completed for user with email={}", user.getEmail());
    }
}
