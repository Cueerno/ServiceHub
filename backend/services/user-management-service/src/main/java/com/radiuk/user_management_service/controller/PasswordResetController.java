package com.radiuk.user_management_service.controller;

import com.radiuk.user_management_service.dto.PasswordResetConfirmDto;
import com.radiuk.user_management_service.dto.PasswordResetRequestDto;
import com.radiuk.user_management_service.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/password/reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<Void> resetPasswordRequest(
            @RequestBody PasswordResetRequestDto passwordResetRequestDto
    ) {
        passwordResetService.resetPasswordRequest(passwordResetRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> resetPasswordConfirm(
            @RequestBody PasswordResetConfirmDto passwordResetConfirmDto
    ) {
        passwordResetService.resetPasswordConfirm(passwordResetConfirmDto);
        return ResponseEntity.ok().build();
    }
}
