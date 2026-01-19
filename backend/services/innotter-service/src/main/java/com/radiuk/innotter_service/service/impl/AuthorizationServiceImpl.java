package com.radiuk.innotter_service.service.impl;

import com.radiuk.innotter_service.entity.PageEntity;
import com.radiuk.innotter_service.proxy.Role;
import com.radiuk.innotter_service.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    public Long getUserIdFromToken(Jwt jwt) {
        Long id = Long.valueOf(jwt.getSubject());
        log.debug("Extracted userId={} from token", id);
        return id;
    }

    public void checkAccess(PageEntity page, Jwt jwt) {
        log.debug("Checking access called: pageId={}, requester={}", page.getId(), jwt.getSubject());
        if (!canAccessUser(page, jwt)) {
            log.warn("Access denied: requester={}, pageId={}", jwt.getSubject(), page.getId());
            throw new AccessDeniedException("Access denied");
        }
    }

    public boolean canAccessUser(PageEntity page, Jwt jwt) {
        if (isAdmin(jwt)) {
            log.debug("Access granted by ADMIN role for requester={}", jwt.getSubject());
            return true;
        }

        boolean owner = isPageOwner(page, jwt);
        log.debug("Access check: owner={}, pageId={}, requester={}", owner, page.getId(), jwt.getSubject());
        return owner;
    }

    public boolean isAdmin(Jwt jwt) {
        boolean admin = getUserRolesFromToken(jwt).contains(Role.ADMIN.name());
        log.debug("isAdmin check for requester={} => {}", jwt.getSubject(), admin);
        return admin;
    }

    public boolean isPageOwner(PageEntity page, Jwt jwt) {
        boolean owner = getUserIdFromToken(jwt).equals(page.getCreatorId());
        log.debug("isPageOwner check: requester={}, pageCreatorId={}, result={}",
                jwt.getSubject(), page.getCreatorId(), owner);
        return owner;
    }

    private List<String> getUserRolesFromToken(Jwt jwt) {
        return jwt.getClaim("authorities");
    }

}
