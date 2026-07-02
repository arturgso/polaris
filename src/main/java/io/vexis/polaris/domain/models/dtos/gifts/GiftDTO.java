package io.vexis.polaris.domain.models.dtos.gifts;

import java.time.Instant;

import io.vexis.polaris.domain.models.dtos.StatusDTO;

public record GiftDTO(
    Long id,
    String title,
    String link,
    String giftFor,
    String event,
    StatusDTO status,
    Instant createdAt,
    Instant updatedAt) {
    }
