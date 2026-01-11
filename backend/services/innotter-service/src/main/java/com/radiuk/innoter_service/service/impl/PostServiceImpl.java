package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.dto.post.PostRequestDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.entity.Post;
import com.radiuk.innoter_service.mapper.PostMapper;
import com.radiuk.innoter_service.repository.PageRepository;
import com.radiuk.innoter_service.repository.PostRepository;
import com.radiuk.innoter_service.service.AuthorizationService;
import com.radiuk.innoter_service.service.PageManagementService;
import com.radiuk.innoter_service.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PageRepository pageRepository;
    private final PageManagementService pageManagementService;
    private final AuthorizationService authorizationService;

    @Override
    public List<PostResponseDto> feed(Jwt jwt) {
        Long requesterId = authorizationService.getUserIdFromToken(jwt);
        log.debug("feed called by userId={}", requesterId);

        List<PageEntity> userPages = pageRepository.findByCreatorId(requesterId);
        log.debug("Found {} pages for userId={}", userPages.size(), requesterId);

        List<Post> userPosts = new ArrayList<>();

        for (PageEntity userPage : userPages) {
            userPosts.addAll(userPage.getPosts());
        }

        log.info("Returning {} posts in feed for userId={}", userPosts.size(), requesterId);
        return userPosts.stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long pageId, Jwt jwt) {
        Long requesterId = authorizationService.getUserIdFromToken(jwt);
        log.debug("Creating post: pageId={}, requesterId={}", pageId, requesterId);

        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);
        Post post = postMapper.fromRequestDto(postRequestDto);

        authorizationService.canAccessUser(page, jwt);

        post.setPage(pageManagementService.getPageByIdOrThrow(pageId));

        Post savedPost = postRepository.save(post);

        log.info("Post created: postId={}, pageId={}, creatorId={}", savedPost.getId(), pageId, requesterId);
        return postMapper.toDto(savedPost);
    }

    @Override
    public PostResponseDto updatePostById(PostRequestDto postRequestDto, Long postId, Jwt jwt) {
        Long requesterId = authorizationService.getUserIdFromToken(jwt);
        log.debug("Updating post: postId={}, requesterId={}", postId, requesterId);

        Post post = getPostByIdOrThrow(postId);

        authorizationService.canAccessUser(post.getPage(), jwt);

        postMapper.updateFromDto(postRequestDto, post);

        log.info("Post updated: postId={}, requesterId={}", postId, requesterId);
        return postMapper.toDto(post);
    }

    @Override
    public void deletePostById(Long postId, Jwt jwt) {
        Long requesterId = authorizationService.getUserIdFromToken(jwt);
        log.debug("Deleting post: postId={}, requesterId={}", postId, requesterId);

        Post post = getPostByIdOrThrow(postId);
        authorizationService.canAccessUser(post.getPage(), jwt);
        postRepository.deleteById(post.getId());

        log.warn("Post deleted: postId={}, requesterId={}", postId, requesterId);
    }

    private Post getPostByIdOrThrow(Long postId) {
        log.debug("Loading post by id={}", postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    log.warn("Post not found: postId={}", postId);
                    return new EntityNotFoundException(String.format("Post with id={%d} not found", postId));
                });
    }
}
