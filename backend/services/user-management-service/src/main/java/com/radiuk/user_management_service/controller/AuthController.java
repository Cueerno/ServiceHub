package com.radiuk.user_management_service.controller;

import com.radiuk.user_management_service.dto.*;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.service.AuthService;
import com.radiuk.user_management_service.service.JwtService;
import com.radiuk.user_management_service.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRegistrationDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            HttpServletResponse httpServletResponse,
            @RequestBody UserAuthDto userAuthDto
    ) {
        AuthResponse authResponse = authService.authenticate(userAuthDto);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.ok(new AuthResponse(authResponse.accessToken(), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response
    ) {
        User user = refreshTokenService.validateAndGetUser(refreshToken);

        JwtWithJti newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user, newAccessToken.jti());


        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new AuthResponse(newAccessToken.accessToken(), null));
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<Void> resetPasswordRequest(
            @AuthenticationPrincipal Jwt jwt
    ) {
        authService.resetPasswordRequest(jwt);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<Void> resetPasswordConfirm(
            ResetPasswordDto resetPasswordDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        authService.resetPasswordConfirm(resetPasswordDto, jwt);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletResponse response
    ) {
        authService.logout(jwt);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }
}













