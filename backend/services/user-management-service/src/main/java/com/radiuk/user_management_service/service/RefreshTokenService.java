package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.entity.User;

public interface RefreshTokenService {

    String createRefreshToken(User user, String jti);

    User validateAndGetUser(String refreshToken);

    void revokeByJti(String jti);

    void revokeAll(User user);
}
