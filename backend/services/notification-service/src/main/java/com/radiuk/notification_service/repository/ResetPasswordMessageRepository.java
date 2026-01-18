package com.radiuk.notification_service.repository;

import com.radiuk.notification_service.entity.ResetPasswordMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordMessageRepository extends JpaRepository<ResetPasswordMessage, Long> {

    List<ResetPasswordMessage> findBySentAtIsNull();
}
