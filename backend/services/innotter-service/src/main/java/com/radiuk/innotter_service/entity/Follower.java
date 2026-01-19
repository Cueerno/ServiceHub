package com.radiuk.innotter_service.entity;

import com.radiuk.innotter_service.entity.embedded_id.FollowerId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "followers", schema = "innotter_service")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follower {

    @EmbeddedId
    private FollowerId id;

    @MapsId("pageId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private PageEntity page;

}