package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.repository.PageRepository;
import com.radiuk.innoter_service.service.PageManagementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageManagementServiceImpl implements PageManagementService {

    private final PageRepository pageRepository;

    @Override
    public PageEntity getPageByIdOrThrow(Long pageId) {
        return pageRepository.findById(pageId)
                .filter(p -> p.getIsBlocked() == false)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Page with id={%d} not found", pageId)));
    }
}
