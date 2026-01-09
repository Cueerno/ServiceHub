package com.radiuk.innoter_service.controller;

import com.radiuk.innoter_service.dto.tag.TagRequestDto;
import com.radiuk.innoter_service.dto.tag.TagResponseDto;
import com.radiuk.innoter_service.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping("")
    public ResponseEntity<List<TagResponseDto>> getPostByTags(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(name = "filter_by_name", required = false) String filterByName
    ) {
        return ResponseEntity.ok(tagService.getTagsWithPaginationAndLimitAndFilter(page, limit, filterByName));
    }

    @PostMapping("")
    public ResponseEntity<TagResponseDto> create(@RequestBody TagRequestDto tagRequestDto) {
        return ResponseEntity.ok(tagService.createTag(tagRequestDto));
    }
}
