package com.radiuk.user_management_service.repository;

import com.radiuk.user_management_service.entity.RefreshToken;
import com.radiuk.user_management_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByJtiAndRevokedFalse(String jti);

    @Modifying
    @Query("update RefreshToken t set t.revoked = true where t.jti = :jti")
    void revokeByJti(@Param("jti") String jti);

    @Modifying
    @Query("update RefreshToken t set t.revoked = true where t.user = :user")
    void revokeAllByUser(@Param("user") User user);

    @Modifying
    @Query("""
        delete from RefreshToken rt
        where rt.expiresAt < :now
           OR rt.revoked = true
    """)
    int deleteExpiredOrRevoked(@Param("now") Instant now);
}
