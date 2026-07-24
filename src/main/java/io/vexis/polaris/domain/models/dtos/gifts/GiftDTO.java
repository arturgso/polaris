package io.vexis.polaris.domain.models.dtos.gifts;

import io.vexis.polaris.domain.models.dtos.StatusDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListBasicInfoDTO;
import java.time.Instant;

public record GiftDTO(
    Long id,
    String title,
    String link,
    String giftFor,
    String event,
    StatusDTO status,
    Instant createdAt,
    Instant updatedAt,
    GiftListBasicInfoDTO giftList) {}
