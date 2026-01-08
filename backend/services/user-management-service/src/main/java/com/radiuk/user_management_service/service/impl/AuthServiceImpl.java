package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.dto.*;
import com.radiuk.user_management_service.entity.Role;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.exception.UserNotCreatedException;
import com.radiuk.user_management_service.mapper.UserMapper;
import com.radiuk.user_management_service.repository.UserRepository;
import com.radiuk.user_management_service.service.AuthService;
import com.radiuk.user_management_service.service.JwtService;
import com.radiuk.user_management_service.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void resetPassword(ResetPasswordDto resetPasswordDto, Jwt jwt) {
        log.debug("Resetting password for user {}", jwt.getClaim("email").toString());

        User user = userRepository.findById(Long.valueOf(jwt.getSubject()))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials, user not found"));

        if (!passwordEncoder.matches(resetPasswordDto.oldPassword(), resetPasswordDto.newPassword())) {
            throw new BadCredentialsException("Invalid credentials: invalid password");
        }

        log.info("Reset password user with email {}", jwt.getClaim("email").toString());
        user.setPassword(passwordEncoder.encode(resetPasswordDto.newPassword()));
    }

    @Override
    public void logout(Jwt jwt) {
        log.info("Logout user {}", jwt.getClaim("email").toString());
        refreshTokenService.revokeByJti(jwt.getId());
    }
}
