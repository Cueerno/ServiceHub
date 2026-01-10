package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.dto.post.PostRequestDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.entity.Post;
import com.radiuk.innoter_service.mapper.PostMapper;
import com.radiuk.innoter_service.repository.PageRepository;
import com.radiuk.innoter_service.repository.PostRepository;
import com.radiuk.innoter_service.service.PageManagementService;
import com.radiuk.innoter_service.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PageRepository pageRepository;
    private final PageManagementService pageManagementService;

    @Override
    public List<PostResponseDto> feed(Jwt jwt) {
        List<PageEntity> userPages = pageRepository.findByCreatorId(Long.valueOf(jwt.getSubject()));

        List<Post> userPosts = new ArrayList<>();

        for (PageEntity userPage : userPages) {
            userPosts.addAll(userPage.getPosts());
        }

        return userPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long pageId, Jwt jwt) {
        Post post = postMapper.fromRequestDto(postRequestDto);
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        post.setPage(pageManagementService.getPageByIdOrThrow(pageId));

        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public PostResponseDto updatePostById(PostRequestDto postRequestDto, Long postId, Jwt jwt) {
        Post post = getPostByIdOrThrow(postId);

        postMapper.updateFromDto(postRequestDto, post);

        return postMapper.toDto(post);
    }

    @Override
    public void deletePostById(Long postId, Long userId) {
        postRepository.deleteById(getPostByIdOrThrow(postId).getId());
    public void deletePostById(Long postId, Jwt jwt) {
        Post post = getPostByIdOrThrow(postId);
        postRepository.deleteById(post.getId());
    }

    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id={%d} not found", postId)));
    }
}
