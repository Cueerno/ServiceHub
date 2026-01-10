package com.radiuk.innoter_service.controller;

import com.radiuk.innoter_service.dto.post.PostRequestDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponseDto>> feed(Long userId) {
        userId = 1L;
        return ResponseEntity.ok(postService.feed(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> update(
            @PathVariable Long id,
            @RequestBody PostRequestDto postRequestDto,
            Long userId
    ) {
        userId = 1L;
        return ResponseEntity.ok(postService.updatePostById(postRequestDto, id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Long userId
    ) {
        userId = 1L;
        postService.deletePostById(id, userId);
        return ResponseEntity.noContent().build();
    }
}