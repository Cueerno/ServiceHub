package com.radiuk.innotter_service.service;

import com.radiuk.innotter_service.entity.PageEntity;

public interface PageManagementService {

    PageEntity getPageByIdOrThrow(Long pageId);
}
