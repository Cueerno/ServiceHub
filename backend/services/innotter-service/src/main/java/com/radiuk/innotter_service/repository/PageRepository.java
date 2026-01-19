package com.radiuk.innotter_service.repository;

import com.radiuk.innotter_service.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {

    boolean existsByName(String name);

    List<PageEntity> findByCreatorId(Long creatorId);
}
