package com.radiuk.innoter_service.service;

import com.radiuk.innoter_service.dto.tag.TagRequestDto;
import com.radiuk.innoter_service.dto.tag.TagResponseDto;

import java.util.List;

public interface TagService {

    List<TagResponseDto> getTagsWithPaginationAndLimitAndFilter(Long page, Long limit, String filterByName);

    TagResponseDto createTag(TagRequestDto tagRequestDto);
}
