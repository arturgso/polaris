package io.vexis.polaris.domain.models.dtos.filters;

public record GiftFiltersDTO(
    Long personId, Long statusId, Long eventId, String title, String link) {}
