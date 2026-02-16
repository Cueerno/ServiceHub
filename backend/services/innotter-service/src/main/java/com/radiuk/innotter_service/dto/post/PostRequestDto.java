package com.radiuk.innotter_service.dto.post;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDto(

        Long id,

        @NotBlank(message = "Post content is required")
        @Size(max = 5000, message = "Post content must not exceed 5000 characters")
        String content,

        @Valid
        PostRequestDto replyTo

) {}