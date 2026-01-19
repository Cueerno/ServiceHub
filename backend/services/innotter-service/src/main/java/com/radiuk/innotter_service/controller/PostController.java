package com.radiuk.innotter_service.controller;

import com.radiuk.innotter_service.dto.post.PostRequestDto;
import com.radiuk.innotter_service.dto.post.PostResponseDto;
import com.radiuk.innotter_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDto>> feed(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(postService.feed(jwt));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> update(
            @PathVariable Long id,
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(postService.updatePostById(postRequestDto, id, jwt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        postService.deletePostById(id, jwt);
        return ResponseEntity.noContent().build();
    }
}