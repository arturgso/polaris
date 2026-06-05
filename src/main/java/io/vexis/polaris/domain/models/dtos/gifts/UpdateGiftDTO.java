package io.vexis.polaris.domain.models.dtos.gifts;

public record UpdateGiftDTO(String title, String link, Long giftFor, Long giftListId, String event, String status) {}
