package com.radiuk.user_management_service.service;

public interface RefreshTokenQueryService {

    boolean isJtiValid(String jti);
}