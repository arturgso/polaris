package io.vexis.polaris.domain.models.dtos.gifts;

public record UpdateGiftDTO(
    String title, String link, String giftFor, String event, String status) {}
