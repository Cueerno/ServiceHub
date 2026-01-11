package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.repository.PageRepository;
import com.radiuk.innoter_service.service.PageManagementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageManagementServiceImpl implements PageManagementService {

    private final PageRepository pageRepository;

    @Override
    @Transactional
    public PageEntity getPageByIdOrThrow(Long pageId) {
        log.debug("Loading page by id={}", pageId);
        return pageRepository.findById(pageId)
                .orElseThrow(() -> {
                    log.warn("Page not found: id={}", pageId);
                    return new EntityNotFoundException(String.format("Page with id={%d} not found", pageId));
                });
    }
}
