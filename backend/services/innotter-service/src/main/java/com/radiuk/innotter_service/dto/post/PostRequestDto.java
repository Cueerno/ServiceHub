package com.radiuk.innotter_service.dto.post;

public record PostRequestDto(
        
        Long id,
        String content,
        PostRequestDto replyTo
) {
}
