package com.radiuk.innoter_service.repository;

import com.radiuk.innoter_service.entity.PageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<PageEntity, Long> {

    boolean existsByName(String name);
}
