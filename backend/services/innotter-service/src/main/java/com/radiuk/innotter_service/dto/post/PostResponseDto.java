package com.radiuk.innotter_service.dto.post;

import java.time.Instant;

public record PostResponseDto(

        String content,
        //PostResponseDto replyTo,
        Instant createdAt,
        Instant updatedAt
        //List<Like> likes

) {
}
