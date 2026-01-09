package com.radiuk.innoter_service.service;

import com.radiuk.innoter_service.dto.page.PageRequestDto;
import com.radiuk.innoter_service.dto.page.PageResponseDto;

import java.util.List;

public interface PageService {

    PageResponseDto getPageById(Long pageId, int page, int limit);

    List<Long> getPageFollowersByPageId(Long pageId, Long userId);

    PageResponseDto createPage(PageRequestDto pageRequestDto, Long userId);

    PageResponseDto updatePage(PageRequestDto pageRequestDto, Long pageId, Long userId);

    PageResponseDto follow(Long pageId, Long userId);

    PageResponseDto unfollow(Long pageId, Long userId);

    void block(Long pageId, Long userId);

    void deletePageById(Long id, Long userId);
}
