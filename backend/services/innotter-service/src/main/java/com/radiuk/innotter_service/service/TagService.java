package com.radiuk.innotter_service.service;

import com.radiuk.innotter_service.dto.tag.TagRequestDto;
import com.radiuk.innotter_service.dto.tag.TagResponseDto;

import java.util.List;

public interface TagService {

    List<TagResponseDto> getTagsWithPaginationAndLimitAndFilter(int page, int limit, String filterByName);

    TagResponseDto createTag(TagRequestDto tagRequestDto);
}
