package com.radiuk.innoter_service.entity;

import com.radiuk.innoter_service.entity.embedded_id.FollowerId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "followers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follower {

    @EmbeddedId
    private FollowerId id;

    @MapsId("pageId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private Page page;

}