package io.vexis.polaris.domain.models.dtos.gifts;

import java.time.Instant;
import java.util.UUID;

public record GiftDTO(
    UUID id,
    String title,
    String link,
    String giftFor,
    String event,
    String status,
    Instant createdAt,
    Instant updatedAt) {}
