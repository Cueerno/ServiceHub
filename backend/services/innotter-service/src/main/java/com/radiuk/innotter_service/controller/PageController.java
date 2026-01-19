package com.radiuk.innotter_service.controller;

import com.radiuk.innotter_service.dto.page.PageRequestDto;
import com.radiuk.innotter_service.dto.page.PageResponseDto;
import com.radiuk.innotter_service.dto.post.PostRequestDto;
import com.radiuk.innotter_service.dto.post.PostResponseDto;
import com.radiuk.innotter_service.service.PageService;
import com.radiuk.innotter_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(pageService.getPageFollowersByPageId(id, jwt));
    }

    @PostMapping("")
    public ResponseEntity<PageResponseDto> create(
            @RequestBody PageRequestDto pageRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(pageService.createPage(pageRequestDto, jwt));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<PostResponseDto> createPost(
            @PathVariable Long id,
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(postService.createPost(postRequestDto, id, jwt));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PageResponseDto> update(
            @PathVariable Long id,
            @RequestBody PageRequestDto pageRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(pageService.updatePage(pageRequestDto, id, jwt));
    }

    @PatchMapping("/{id}/follow")
    public ResponseEntity<PageResponseDto> follow(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(pageService.follow(id, jwt));
    }

    @PatchMapping("/{id}/unfollow")
    public ResponseEntity<PageResponseDto> unfollow(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(pageService.unfollow(id, jwt));
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<PageResponseDto> block(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        pageService.block(id, jwt);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        pageService.deletePageById(id, jwt);
        return ResponseEntity.noContent().build();
    }
}
