package io.vexis.polaris.domain.models.dtos.gifts;

import io.vexis.polaris.domain.enums.Events;
import io.vexis.polaris.domain.enums.GiftStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record NewGiftDTO(
        @NotBlank String title,
        String link,
        @NotBlank String giftFor,
        Events event,
        GiftStatus status
) {
}
