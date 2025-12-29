package com.radiuk.innoter_service.service.impl;

import com.radiuk.innoter_service.dto.follower.FollowerResponseDto;
import com.radiuk.innoter_service.dto.page.PageRequestDto;
import com.radiuk.innoter_service.dto.page.PageResponseDto;
import com.radiuk.innoter_service.dto.post.PostResponseDto;
import com.radiuk.innoter_service.entity.Follower;
import com.radiuk.innoter_service.entity.PageEntity;
import com.radiuk.innoter_service.entity.Post;
import com.radiuk.innoter_service.entity.embedded_id.FollowerId;
import com.radiuk.innoter_service.mapper.FollowerMapper;
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
    private final FollowerMapper followerMapper;

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
                //pageEntity.getFollowers().stream().map(followerMapper::toDto).toList(),
                postResponseDtos,
                pageEntity.getTags().stream().map(tagMapper::toDto).toList()
        );
    }

    @Override
    public List<FollowerResponseDto> getPageFollowersByPageId(Long pageId) {
        PageEntity page = pageRepository.findById(pageId)
                .filter(p -> p.getIsBlocked() == false)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Page with id={%d} not found", pageId)));

        List<Long> userIds = new ArrayList<>();

        for (Follower follower : page.getFollowers()) {
            userIds.add(follower.getId().getUserId());
        }

        //feign client user-servive - List

        return null;
    }

    @Override
    public PageResponseDto createPage(PageRequestDto pageRequestDto, Long userId) {
        PageEntity page = pageMapper.fromRequestDto(pageRequestDto);

        return pageMapper.toDto(pageRepository.save(page));
    }

    @Override
    public PageResponseDto updatePage(PageRequestDto pageRequestDto, Long pageId, Long userId) {
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        pageMapper.updateFromDto(pageRequestDto, page);

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
    public PageResponseDto block(Long pageId, Long userId) {
        PageEntity page = pageManagementService.getPageByIdOrThrow(pageId);

        page.setIsBlocked(true);

        return pageMapper.toDto(page);
    }

    @Override
    public void deletePageById(Long pageId) {
        pageRepository.deleteById(pageManagementService.getPageByIdOrThrow(pageId).getId());
    }


}
