package com.radiuk.innotter_service.repository;

import com.radiuk.innotter_service.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Page<Tag> findByNameContainingIgnoreCase(Pageable pageable, String filterByName);

    boolean existsByName(String name);
}