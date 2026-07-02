package io.vexis.polaris.domain.models.dtos.gifts;

import io.vexis.polaris.domain.models.dtos.StatusDTO;
import java.time.Instant;

public record GiftDTO(
    Long id,
    String title,
    String link,
    String giftFor,
    String event,
    StatusDTO status,
    Instant createdAt,
    Instant updatedAt) {}
