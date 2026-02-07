package com.radiuk.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtJtiValidationFilter implements WebFilter {

    private static final String PREFIX = "refresh:jti:";
    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {

                    if (auth.getPrincipal() instanceof Jwt jwt) {
                        String jti = jwt.getId();

                        if (jti == null) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        return redisTemplate.hasKey(PREFIX + jti)
                                .flatMap(exists -> {
                                    if (Boolean.FALSE.equals(exists)) {
                                        exchange.getResponse()
                                                .setStatusCode(HttpStatus.UNAUTHORIZED);
                                        return exchange.getResponse().setComplete();
                                    }
                                    return chain.filter(exchange);
                                });
                    }

                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}