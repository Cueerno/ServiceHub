package com.radiuk.user_management_service.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class ResetTokenGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generate() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
