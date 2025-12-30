package com.radiuk.user_management_service.service;

import com.radiuk.user_management_service.dto.JwtWithJti;
import com.radiuk.user_management_service.entity.User;

public interface JwtService {

    JwtWithJti generateToken(User user);
}
