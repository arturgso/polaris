package io.vexis.polaris.domain.models.dtos.gifts;

import io.vexis.polaris.domain.enums.GiftStatus;

public record UpdateGiftDTO(
    String title, String link, Long giftFor, Long giftListId, String event, GiftStatus status) {}
