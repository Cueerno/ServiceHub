package com.radiuk.innotter_service.dto.page;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record BlockPageRequestDto(

        @NotNull(message = "Unblock date is required")
        @Future(message = "Unblock date must be in the future")
        Instant unblockDate

) {}