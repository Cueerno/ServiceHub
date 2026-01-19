package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user, String jti);

    User validateAndGetUser(String refreshToken, String jti);

    void revokeByJti(String jti);
}
