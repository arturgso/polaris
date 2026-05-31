package io.vexis.polaris.domain.models.dtos.gifts;

import java.time.Instant;

public record GiftDTO(
    Long id,
    String title,
    String link,
    String giftFor,
    String event,
    String status,
    Instant createdAt,
    Instant updatedAt) {}
