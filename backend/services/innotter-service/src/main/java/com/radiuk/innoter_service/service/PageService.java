package com.radiuk.innoter_service.service;

import com.radiuk.innoter_service.dto.page.PageRequestDto;
import com.radiuk.innoter_service.dto.page.PageResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface PageService {

    PageResponseDto getPageById(Long pageId, int page, int limit);

    List<Long> getPageFollowersByPageId(Long pageId, Jwt jwt);

    PageResponseDto createPage(PageRequestDto pageRequestDto, Jwt jwt);

    PageResponseDto updatePage(PageRequestDto pageRequestDto, Long pageId, Jwt jwt);

    PageResponseDto follow(Long pageId, Jwt jwt);

    PageResponseDto unfollow(Long pageId, Jwt jwt);

    void block(Long pageId, Jwt jwt);

    void deletePageById(Long id, Jwt jwt);
}
