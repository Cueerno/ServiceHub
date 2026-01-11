package com.radiuk.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@Table(name = "reset_password_messages")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ResetPasswordMessage {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 100)
    private String emailAddress;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String body;

    @CreatedDate
    @Column(nullable = false)
    private Instant publishedAt;

   // @Column(nullable = false)
    private Instant sentAt;

}