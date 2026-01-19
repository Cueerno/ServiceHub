package com.radiuk.user_management_service.repository;

import com.radiuk.user_management_service.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("""
        delete from PasswordResetToken prt
        where prt.expiresAt < :now
            or prt.used = true
    """)
    int deleteExpiredOrUsed(@Param("now") Instant now);

}