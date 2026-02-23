package com.radiuk.innotter_service.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequestDto(

        @NotBlank(message = "Tag name is required")
        @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
        String name

) {}