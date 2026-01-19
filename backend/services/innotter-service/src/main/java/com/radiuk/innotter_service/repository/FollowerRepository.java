package com.radiuk.innotter_service.repository;

import com.radiuk.innotter_service.entity.Follower;
import com.radiuk.innotter_service.entity.embedded_id.FollowerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, FollowerId> {
}