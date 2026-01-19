package com.radiuk.innotter_service.service.impl;

import com.radiuk.innotter_service.dto.page.PageRequestDto;
import com.radiuk.innotter_service.dto.page.PageResponseDto;
import com.radiuk.innotter_service.dto.post.PostResponseDto;
import com.radiuk.innotter_service.entity.Follower;
import com.radiuk.innotter_service.entity.PageEntity;
import com.radiuk.innotter_service.entity.Post;
import com.radiuk.innotter_service.entity.embedded_id.FollowerId;
import com.radiuk.innotter_service.exception.PageNotCreatedException;
import com.radiuk.innotter_service.exception.PageNotUpdatedException;
import com.radiuk.innotter_service.mapper.PageMapper;
import com.radiuk.innotter_service.mapper.PostMapper;
import com.radiuk.innotter_service.mapper.TagMapper;
import com.radiuk.innotter_service.repository.FollowerRepository;
import com.radiuk.innotter_service.repository.PageRepository;
import com.radiuk.innotter_service.repository.PostRepository;
import com.radiuk.innotter_service.service.AuthorizationService;
import com.radiuk.innotter_service.service.PageManagementService;
import com.radiuk.innotter_service.service.PageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final PageManagementService pageManagementService;
    private final PageMapper pageMapper;
    private final PageRepository pageRepository;
    private final PostRepository postRepository;
    private final FollowerRepository followerRepository;
    private final TagMapper tagMapper;
    private final PostMapper postMapper;
    private final AuthorizationService authorizationService;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto getPageById(Long pageId, int page, int limit) {
        log.debug("Getting pages: pageId={}, page={}, limit={}", pageId, page, limit);
        PageEntity pageEntity = pageManagementService.getPageByIdOrThrow(pageId);

        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Post> postsPage = postRepository.findByPageId(pageEntity.getId(), pageable);

        List<PostResponseDto> postResponseDtos = postsPage
                .getContent()
                .stream().map(postMapper::toDto)
                .toList();

        log.info("Returning page id={}, name={}", pageEntity.getId(), pageEntity.getName());
        return new PageResponseDto(
                pageEntity.getName(),
                pageEntity.getDescription(),
                pageEntity.getImageUrl(),
                pageEntity.getUnblockDate(),
                pageEntity.getCreatedAt(),
                pageEntity.getUpdatedAt(),
                postResponseDtos,
                pageEntity.getTags().stream().map(tagMapper::toDto).toList()
        );
    }

    @Override
    public List<Long> getPageFollowersByPageId(Long pageId, Jwt jwt) {
        log.debug("Getting page followers: pageId={}, requester={}", pageId, jwt.getSubject());
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        authorizationService.checkAccess(page, jwt);

        List<Long> userIds = new ArrayList<>();

        for (Follower follower : page.getFollowers()) {
            userIds.add(follower.getId().getUserId());
        }

        log.info("Followers count for pageId={} => {}", pageId, userIds.size());
        return userIds;
    }

    @Override
    public PageResponseDto createPage(PageRequestDto pageRequestDto, Jwt jwt) {
        log.debug("Creating page called by requester={}, name={}", jwt.getSubject(), pageRequestDto.name());

        if (pageRepository.existsByName(pageRequestDto.name())) {
            log.warn("Page creation failed: name already exists '{}'", pageRequestDto.name());
            throw new PageNotCreatedException(String.format("Page with name %s already exists", pageRequestDto.name()));
        }

        PageEntity page = pageMapper.fromRequestDto(pageRequestDto);

        page.setCreatorId(getUserIdFromToken(jwt));

        PageEntity saved = pageRepository.save(page);

        log.info("Page created: id={}, name={}, creatorId={}", saved.getId(), saved.getName(), saved.getCreatorId());
        return pageMapper.toDto(saved);
    }

    @Override
    public PageResponseDto updatePage(PageRequestDto dto, Long pageId, Jwt jwt) {
        log.debug("Updating page: pageId={}, requester={}", pageId, jwt.getSubject());

        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        authorizationService.checkAccess(page, jwt);

        String newPageName = dto.name();

        if (newPageName != null && !newPageName.equals(page.getName()) && pageRepository.existsByName(dto.name())) {
            log.warn("Page update failed: name '{}' already exists", newPageName);
            throw new PageNotUpdatedException(String.format("Page with name %s already exists", newPageName));
        }

        pageMapper.updateFromDto(dto, page);

        log.info("Page updated: pageId={}, requester={}", pageId, jwt.getSubject());
        return pageMapper.toDto(page);
    }

    @Override
    public PageResponseDto follow(Long pageId, Jwt jwt) {
        Long requesterId = getUserIdFromToken(jwt);
        log.debug("User following on the page: pageId={}, requesterId={}", pageId, requesterId);

        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);
        Follower follower = new Follower(new FollowerId(pageId, requesterId), page);
        followerRepository.save(follower);

        log.info("User {} followed page {}", requesterId, pageId);
        return pageMapper.toDto(page);
    }

    @Override
    public PageResponseDto unfollow(Long pageId, Jwt jwt) {
        Long requesterId = getUserIdFromToken(jwt);
        log.debug("User unfollowing on the page: pageId={}, requesterId={}", pageId, requesterId);

        followerRepository.deleteById(new FollowerId(pageId, requesterId));

        log.info("User {} unfollowed page {}", requesterId, pageId);
        return pageMapper.toDto(pageManagementService.getPageByIdOrThrow(pageId));
    }

    @Override
    public void block(Long pageId, Jwt jwt) {
        log.debug("Blocking page: pageId={}, requester={}", pageId, jwt.getSubject());

        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        authorizationService.checkAccess(page, jwt);

        page.setBlocked(true);
        log.warn("Page blocked: pageId={}, by={}", pageId, jwt.getSubject());
    }

    @Override
    public void deletePageById(Long pageId, Jwt jwt) {
        log.debug("Deleting page: pageId={}, requester={}", pageId, jwt.getSubject());
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);
        authorizationService.canAccessUser(page, jwt);
        pageRepository.deleteById(page.getId());
        log.warn("Page deleted: pageId={}, requester={}", pageId, jwt.getSubject());
    }

    private Long getUserIdFromToken(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}
