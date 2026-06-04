package io.vexis.polaris.domain.models.dtos.giftlist;

import java.time.Instant;
import java.util.List;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;

public record GiftListDTO(
    Long id,
    String title,
    List<GiftDTO> gifts,
    Instant createdAt,
    Instant updatedAt
) {

}