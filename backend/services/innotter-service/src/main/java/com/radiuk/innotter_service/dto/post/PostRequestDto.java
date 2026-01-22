package com.radiuk.innotter_service.dto.post;

public record PostRequestDto(

        String content,
        Long replyTo
) {
}
