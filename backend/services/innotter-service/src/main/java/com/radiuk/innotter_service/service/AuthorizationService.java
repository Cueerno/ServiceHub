package com.radiuk.innotter_service.service;

import com.radiuk.innotter_service.entity.PageEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthorizationService {

    Long getUserIdFromToken(Jwt jwt);

    boolean canAccessUser(PageEntity page, Jwt jwt);

    void checkAccess(PageEntity page, Jwt jwt);

    boolean isAdmin(Jwt jwt);

    boolean isPageOwner(PageEntity page, Jwt jwt);
}
