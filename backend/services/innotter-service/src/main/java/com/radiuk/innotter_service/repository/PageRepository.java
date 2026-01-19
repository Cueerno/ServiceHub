package com.radiuk.innotter_service.repository;

import com.radiuk.innotter_service.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {

    boolean existsByName(String name);

    List<PageEntity> findByCreatorId(Long creatorId);

    @Query("""
        select
              p
        from PageEntity p
        where p.isBlocked = true and p.unblockDate <= :now
        """)
    List<PageEntity> findAllByBlockedTrueAndUnblockDateBefore(@Param("now") Instant now);
}
