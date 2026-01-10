package com.radiuk.innoter_service.controller;

import com.radiuk.innoter_service.dto.page.PageRequestDto;
import com.radiuk.innoter_service.dto.page.PageResponseDto;
import com.radiuk.innoter_service.dto.post.PostRequestDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.service.PageService;
import com.radiuk.innoter_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pages")
public class PageController {

    private final PageService pageService;
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PageResponseDto> getPageByIdWithPageableAndLimit(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int limit
    ) {
        return ResponseEntity.ok(pageService.getPageById(id, page, limit));
    }

    @GetMapping("/{id}/followers")
    public ResponseEntity<List<Long>> getPageFollowers(
            @PathVariable Long id,
            Long userId
    ) {
        userId = 1L;
        return ResponseEntity.ok(pageService.getPageFollowersByPageId(id, userId));
    }

    @PostMapping("")
    public ResponseEntity<PageResponseDto> create(@RequestBody PageRequestDto pageRequestDto, Long userId) {
        userId = 1L;
        return ResponseEntity.ok(pageService.createPage(pageRequestDto, userId));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<PostResponseDto> createPost(
            @PathVariable Long id,
            @RequestBody PostRequestDto postRequestDto,
            Long userId
    ) {
        userId = 1L;
        return ResponseEntity.ok(postService.createPost(postRequestDto, id, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PageResponseDto> update(
            @PathVariable Long id,
            @RequestBody PageRequestDto pageRequestDto,
            Long userId
    ) {
        userId = 1L;
        return ResponseEntity.ok(pageService.updatePage(pageRequestDto, id, userId));
    }

    @PatchMapping("/{id}/follow")
    public ResponseEntity<PageResponseDto> follow(@PathVariable Long id, Long userId) {
        userId = 1L;
        return ResponseEntity.ok(pageService.follow(id, userId));
    }

    @PatchMapping("/{id}/unfollow")
    public ResponseEntity<PageResponseDto> unfollow(@PathVariable Long id, Long userId) {
        userId = 1L;
        return ResponseEntity.ok(pageService.unfollow(id, userId));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<PageResponseDto> block(@PathVariable Long id, Long userId) {
        userId = 1L;
        pageService.block(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Long userId) {
        userId = 1L;
        pageService.deletePageById(id, userId);
        return ResponseEntity.noContent().build();
    }
}
