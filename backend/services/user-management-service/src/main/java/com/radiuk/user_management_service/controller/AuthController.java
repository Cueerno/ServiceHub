package com.radiuk.user_management_service.controller;

import com.radiuk.user_management_service.dto.*;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.service.AuthService;
import com.radiuk.user_management_service.service.JwtService;
import com.radiuk.user_management_service.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.refresh-token-ttl}")
    private Duration refreshTokenTtl;

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(userRegistrationDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            HttpServletResponse response,
            @RequestBody UserAuthDto userAuthDto
    ) {
        AuthResponse authResponse = authService.authenticate(userAuthDto);

        addRefreshCookies(response, authResponse.refreshToken(), authResponse.accessToken().jti());

        return ResponseEntity.ok(new AuthResponse(new JwtWithJti(authResponse.accessToken().jwt(), null), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue("refreshToken") String refreshToken,
            @CookieValue("refreshJti") String jti,
            HttpServletResponse response
    ) {
        User user = refreshTokenService.validateAndGetUser(refreshToken, jti);

        JwtWithJti newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user, newAccessToken.jti());

        addRefreshCookies(response, newRefreshToken, newAccessToken.jti());

        return ResponseEntity.ok(new AuthResponse(new JwtWithJti(newAccessToken.jwt(), null), null));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Jwt jwt,
            HttpServletResponse response
    ) {
        authService.logout(jwt);

        deleteRefreshCookies(response);

        return ResponseEntity.noContent().build();
    }

    private void addRefreshCookies(HttpServletResponse response, String refreshToken, String jti) {
        ResponseCookie tokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .maxAge(refreshTokenTtl)
                .build();

        ResponseCookie jtiCookie = ResponseCookie.from("refreshJti", jti)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/v1/auth/refresh")
                .maxAge(refreshTokenTtl)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jtiCookie.toString());
    }

    private void deleteRefreshCookies(HttpServletResponse response) {
        ResponseCookie tokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .build();

        ResponseCookie jtiCookie = ResponseCookie.from("refreshJti", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, jtiCookie.toString());
    }
}













