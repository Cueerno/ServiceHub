package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.dto.tag.TagRequestDto;
import com.radiuk.innoter_service.dto.tag.TagResponseDto;
import com.radiuk.innoter_service.entity.Tag;
import com.radiuk.innoter_service.mapper.TagMapper;
import com.radiuk.innoter_service.repository.TagRepository;
import com.radiuk.innoter_service.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagResponseDto> getTagsWithPaginationAndLimitAndFilter(int page, int limit, String filterByName) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "name"));

        return (
                (filterByName == null || filterByName.isBlank())
                        ? tagRepository.findAll(pageable).getContent()
                        : tagRepository.findByNameContainingIgnoreCase(pageable, filterByName).getContent()
        )
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }

    @Override
    public TagResponseDto createTag(TagRequestDto tagRequestDto) {
        Tag tag = tagMapper.fromRequestDto(tagRequestDto);

        return tagMapper.toDto(tagRepository.save(tag));
    }
}
