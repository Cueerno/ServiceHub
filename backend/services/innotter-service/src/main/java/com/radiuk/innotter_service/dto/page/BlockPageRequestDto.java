package com.radiuk.innotter_service.dto.page;

import java.time.Instant;

public record BlockPageRequestDto(

        Instant unblockDate
) {
}
