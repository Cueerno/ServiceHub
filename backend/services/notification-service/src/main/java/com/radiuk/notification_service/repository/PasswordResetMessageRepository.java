package com.radiuk.notification_service.repository;

import com.radiuk.notification_service.entity.PasswordResetMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordResetMessageRepository extends JpaRepository<PasswordResetMessage, Long> {

    List<PasswordResetMessage> findBySentAtIsNull();
}
