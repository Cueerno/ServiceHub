package com.radiuk.innoter_service.service;

import com.radiuk.innoter_service.dto.post.PostRequestDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PostService {

    List<PostResponseDto> feed(Long userId);

    PostResponseDto createPost(PostRequestDto postRequestDto, Long pageId, Long userId);

    PostResponseDto updatePostById(PostRequestDto postRequestDto, @PathVariable Long postId, Long userId);

    void deletePostById(Long postId, Long userId);
}
