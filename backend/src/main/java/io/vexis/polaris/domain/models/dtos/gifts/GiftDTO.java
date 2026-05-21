package io.vexis.polaris.domain.models.dtos.gifts;

import io.vexis.polaris.domain.enums.Events;
import io.vexis.polaris.domain.enums.GiftStatus;

import java.time.Instant;
import java.util.UUID;

public record GiftDTO(
        UUID id,
        String title,
        String link,
        String giftFor,
        Events event,
        GiftStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
