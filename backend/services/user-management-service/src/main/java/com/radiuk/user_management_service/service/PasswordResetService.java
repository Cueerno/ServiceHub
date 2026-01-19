package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.dto.PasswordResetConfirmDto;
import com.radiuk.user_management_service.dto.PasswordResetRequestDto;

public interface PasswordResetService {

    void resetPasswordRequest(PasswordResetRequestDto passwordResetRequestDto);

    void resetPasswordConfirm(PasswordResetConfirmDto passwordResetConfirmDto);
}
