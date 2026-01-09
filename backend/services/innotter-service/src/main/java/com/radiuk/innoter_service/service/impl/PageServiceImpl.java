package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.dto.page.PageRequestDto;
import com.radiuk.innoter_service.dto.page.PageResponseDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.entity.Follower;
import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.entity.Post;
import com.radiuk.innoter_service.entity.embedded_id.FollowerId;
import com.radiuk.innoter_service.exception.PageNotCreatedException;
import com.radiuk.innoter_service.exception.PageNotUpdatedException;
import com.radiuk.innoter_service.mapper.PageMapper;
import com.radiuk.innoter_service.mapper.PostMapper;
import com.radiuk.innoter_service.mapper.TagMapper;
import com.radiuk.innoter_service.repository.FollowerRepository;
import com.radiuk.innoter_service.repository.PageRepository;
import com.radiuk.innoter_service.repository.PostRepository;
import com.radiuk.innoter_service.service.PageManagementService;
import com.radiuk.innoter_service.service.PageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto getPageById(Long pageId, int page, int limit) {
        PageEntity pageEntity = pageManagementService.getPageByIdOrThrow(pageId);

        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Post> postsPage = postRepository.findByPageId(pageEntity.getId(), pageable);

        List<PostResponseDto> postResponseDtos = postsPage
                .getContent()
                .stream().map(postMapper::toDto)
                .toList();

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
    public List<Long> getPageFollowersByPageId(Long pageId, Long userId) {
        PageEntity page = pageRepository.findById(pageId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Page with id={%d} not found", pageId)));

        List<Long> userIds = new ArrayList<>();

        for (Follower follower : page.getFollowers()) {
            userIds.add(follower.getId().getUserId());
        }

        return userIds;
    }

    @Override
    public PageResponseDto createPage(PageRequestDto pageRequestDto, Long userId) {
        if (pageRepository.existsByName(pageRequestDto.name())) {
            throw new PageNotCreatedException(String.format("Page with name %s already exists", pageRequestDto.name()));
        }

        PageEntity page = pageMapper.fromRequestDto(pageRequestDto);

        page.setCreatorId(userId);

        return pageMapper.toDto(pageRepository.save(page));
    }

    @Override
    public PageResponseDto updatePage(PageRequestDto dto, Long pageId, Long userId) {
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        String newPageName = dto.name();

        if (newPageName != null && !newPageName.equals(page.getName()) && pageRepository.existsByName(dto.name())) {
            throw new PageNotUpdatedException(String.format("Page with name %s already exists", newPageName));
        }

        pageMapper.updateFromDto(dto, page);

        return pageMapper.toDto(page);
    }

    @Override
    public PageResponseDto follow(Long pageId, Long userId) {
        FollowerId followerId = new FollowerId(pageId, userId);
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        Follower follower = new Follower(followerId, page);

        followerRepository.save(follower);

        return pageMapper.toDto(page);
    }

    @Override
    public PageResponseDto unfollow(Long pageId, Long userId) {
        FollowerId followerId = new FollowerId(pageId, userId);

        followerRepository.deleteById(followerId);

        return pageMapper.toDto(pageManagementService.getPageByIdOrThrow(pageId));
    }

    @Override
    public void block(Long pageId, Long userId) {
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        page.setBlocked(true);
    }

    @Override
    public void deletePageById(Long pageId, Long userId) {
        pageRepository.deleteById(pageManagementService.getPageByIdOrThrow(pageId).getId());
    }
}
