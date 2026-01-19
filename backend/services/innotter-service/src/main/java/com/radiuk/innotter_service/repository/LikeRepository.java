package com.radiuk.innotter_service.repository;

import com.radiuk.innotter_service.entity.Like;
import com.radiuk.innotter_service.entity.embedded_id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
}