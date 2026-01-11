package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.dto.tag.TagRequestDto;
import com.radiuk.innoter_service.dto.tag.TagResponseDto;
import com.radiuk.innoter_service.entity.Tag;
import com.radiuk.innoter_service.exception.TagNotCreatedException;
import com.radiuk.innoter_service.mapper.TagMapper;
import com.radiuk.innoter_service.repository.TagRepository;
import com.radiuk.innoter_service.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagResponseDto> getTagsWithPaginationAndLimitAndFilter(int page, int limit, String filterByName) {
        log.debug("Get tags called: page={}, limit={}, filterByName={}", page, limit, filterByName);

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "name"));

        List<TagResponseDto> result = (
                (filterByName == null || filterByName.isBlank())
                        ? tagRepository.findAll(pageable).getContent()
                        : tagRepository.findByNameContainingIgnoreCase(pageable, filterByName).getContent()
        )
                .stream()
                .map(tagMapper::toDto)
                .toList();

        log.info("Returning {} tags (page={}, limit={}, filter={})", result.size(), page, limit, filterByName);
        return result;
    }

    @Override
    public TagResponseDto createTag(TagRequestDto dto) {
        log.debug("Creating tag with name={}", dto.name());

        Tag tag = tagMapper.fromRequestDto(dto);

        if (tagRepository.existsByName(dto.name())) {
            log.warn("Tag creation failed: tag with name '{}' already exists", dto.name());
            throw new TagNotCreatedException("Tag already exists");
        }

        log.info("Tag created with name={}", dto.name());
        return tagMapper.toDto(tagRepository.save(tag));
    }
}
