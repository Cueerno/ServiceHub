package com.radiuk.innotter_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "pages", schema = "innotter_service")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(length = Integer.MAX_VALUE)
    private String imageUrl;

    @Column(nullable = false)
    private boolean isBlocked;

    private Instant unblockDate;

    @CreatedDate
    @Column(nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    private Long creatorId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private List<Follower> followers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pages_tags",
            schema = "innotter_service",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

}
