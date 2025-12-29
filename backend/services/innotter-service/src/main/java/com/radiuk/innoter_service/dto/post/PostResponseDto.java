package com.radiuk.innoter_service.dto.post;

import java.time.Instant;
import java.util.List;

public record PostResponseDto(

        String content,
        //PostResponseDto replyTo,
        Instant createdAt,
        Instant updatedAt
        //List<Like> likes

) {
}
