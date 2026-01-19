package com.radiuk.innotter_service.dto.page;

import com.radiuk.innotter_service.dto.post.PostResponseDto;
import com.radiuk.innotter_service.dto.tag.TagResponseDto;

import java.time.Instant;
import java.util.List;

public record PageResponseDto(

        String name,
        String description,
        String imageUrl,
        Instant unblockDate,
        Instant createdAt,
        Instant updatedAt,
        //List<FollowerResponseDto> followers,
        List<PostResponseDto> posts,
        List<TagResponseDto> tags
) {
}
