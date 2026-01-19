package com.radiuk.innotter_service.service;

import com.radiuk.innotter_service.dto.post.PostRequestDto;
import com.radiuk.innotter_service.dto.post.PostResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PostService {

    List<PostResponseDto> feed(Jwt jwt);

    PostResponseDto createPost(PostRequestDto postRequestDto, Long pageId, Jwt jwt);

    PostResponseDto updatePostById(PostRequestDto postRequestDto, @PathVariable Long postId, Jwt jwt);

    void deletePostById(Long postId, Jwt jwt);
}
