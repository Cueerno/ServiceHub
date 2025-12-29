package com.radiuk.innoter_service.service;

import com.radiuk.innoter_service.entity.PageEntity;

public interface PageManagementService {

    PageEntity getPageByIdOrThrow(Long pageId);
}
