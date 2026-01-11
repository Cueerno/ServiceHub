package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.proxy.Role;
import com.radiuk.innoter_service.service.AuthorizationService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    public Long getUserIdFromToken(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }

    public void checkAccess(PageEntity page, Jwt jwt) {
        if (!canAccessUser(page, jwt)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public boolean canAccessUser(PageEntity page, Jwt jwt) {
        if (isAdmin(jwt)) {
            return true;
        }

        return isPageOwner(page, jwt);
    }

    public boolean isAdmin(Jwt jwt) {
    }

    public boolean isPageOwner(PageEntity page, Jwt jwt) {
        return getUserIdFromToken(jwt).equals(page.getCreatorId());
    }

    private List<String> getUserRolesFromToken(Jwt jwt) {
        return jwt.getClaim("authorities");
    }

}
