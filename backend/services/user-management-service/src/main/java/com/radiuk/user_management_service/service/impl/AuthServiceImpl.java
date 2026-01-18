package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.dto.*;
import com.radiuk.user_management_service.entity.PasswordResetToken;
import com.radiuk.user_management_service.entity.Role;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.event.ResetPasswordEvent;
import com.radiuk.user_management_service.exception.UserNotCreatedException;
import com.radiuk.user_management_service.mapper.UserMapper;
import com.radiuk.user_management_service.repository.PasswordResetTokenRepository;
import com.radiuk.user_management_service.repository.UserRepository;
import com.radiuk.user_management_service.service.AuthService;
import com.radiuk.user_management_service.service.JwtService;
import com.radiuk.user_management_service.service.RefreshTokenService;
import com.radiuk.user_management_service.util.ResetTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ResetTokenGenerator resetTokenGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final Duration TOKEN_TTL = Duration.ofMinutes(10);

    @Override
    public UserResponseDto register(UserRegistrationDto userRegistrationDto) {
        log.debug("Registering user with email {}", userRegistrationDto.email());

        if (userRepository.existsByEmail(userRegistrationDto.email())) {
            throw new UserNotCreatedException("User with this email already exists");
        }

        if (userRepository.existsByUsername((userRegistrationDto.username()))) {
            throw new UserNotCreatedException("User with this username already exists");
        }

        User user = userMapper.fromRegistrationDto(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.password()));
        user.setRole(Role.USER);

        log.info("User {} registered successfully", user.getEmail());

        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Override
    public AuthResponse authenticate(UserAuthDto dto) {
        log.debug("Authenticating user with login {}", dto.login());

        boolean isEmail = dto.login().contains("@");

        User user = (isEmail)
                ? userRepository.findByEmail(dto.login())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials, user not found"))
                : userRepository.findByUsername(dto.login())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials, user not found"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials: invalid password");
        }

        JwtWithJti jwt = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user, jwt.jti());

        log.info("Authenticated user with email {}", dto.login());
        return new AuthResponse(jwt.accessToken(), refreshToken);
    }

    @Override
    public void resetPasswordRequest(PasswordResetRequestDto dto) {
        userRepository.findByEmail(dto.email()).ifPresent(user -> {
            String token = resetTokenGenerator.generate();

            PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                    .token(token)
                    .userId(user.getId())
                    .expiresAt(Instant.now().plus(TOKEN_TTL))
                    .used(false)
                    .build();

            passwordResetTokenRepository.save(passwordResetToken);

            applicationEventPublisher.publishEvent(
                    new ResetPasswordEvent(
                            user.getId(),
                            user.getEmail(),
                            token,
                            passwordResetToken.getExpiresAt()
                    )
            );
        });
    }


    @Override
    public void resetPasswordConfirm(PasswordResetConfirmDto dto) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(dto.token())
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isUsed()) {
            throw new IllegalStateException("Token already used");
        }

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Token expired");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        resetToken.setUsed(true);
    }

    @Override
    public void logout(Jwt jwt) {
        log.info("Logout user {}", jwt.getClaim("email").toString());
        refreshTokenService.revokeByJti(jwt.getId());
    }
}
