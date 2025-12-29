package com.radiuk.innoter_service.repository;

import com.radiuk.innoter_service.entity.Like;
import com.radiuk.innoter_service.entity.embedded_id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
}