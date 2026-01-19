package com.radiuk.user_management_service.service.impl;

import com.radiuk.user_management_service.dto.JwtWithJti;
import com.radiuk.user_management_service.entity.User;
import com.radiuk.user_management_service.service.JwtService;
import com.radiuk.user_management_service.util.JwtClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.access-token-ttl}")
    private Duration accessTokenTtl;

    @Value("${jwt.issuer}")
    private String issuer;

    private final JwtEncoder jwtEncoder;

    @Override
    public JwtWithJti generateToken(User user) {
        log.debug("Generating JWT for user with email {}", user.getEmail());

        Instant now = Instant.now();
        String jti = UUID.randomUUID().toString();

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .subject(user.getId().toString())
                .expiresAt(now.plus(accessTokenTtl))
                .claim(JwtClaims.EMAIL, user.getEmail())
                .claim(JwtClaims.AUTHORITIES, List.of(user.getRole().name()))
                .id(jti);

        if (user.getGroup() != null) {
            claimsBuilder.claim(JwtClaims.GROUP_ID, user.getGroup().getId());
        }

        JwtClaimsSet claims = claimsBuilder.build();

        String accessToken = jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(), claims
                )
        ).getTokenValue();

        log.debug("Generated JWT for user with email {}", user.getEmail());
        return new JwtWithJti(accessToken, jti);
    }
}
