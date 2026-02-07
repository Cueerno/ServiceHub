package com.radiuk.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtJtiValidationFilter jwtJtiValidationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers(
                                "/actuator/**",
                                "/api/v1/auth/**",
                                "/api/v1/password/reset/**"
                        ).permitAll()
                        .anyExchange().permitAll()
                )

                .addFilterAfter(
                        jwtJtiValidationFilter,
                        SecurityWebFiltersOrder.AUTHENTICATION
                );

        return http.build();
    }
}