package com.radiuk.innotter_service.dto.page;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PageRequestDto(

        @NotBlank(message = "Page name is required")
        @Size(min = 3, max = 100, message = "Page name must be between 3 and 100 characters")
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Pattern(
                regexp = "^(https?://).+$",
                message = "Image URL must be a valid HTTP or HTTPS link"
        )
        String imageUrl

) {
}
