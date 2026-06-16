package io.vexis.polaris.domain.models.dtos.filters;

public record GiftFiltersDTO(
    Long personId, String status, String event, String title, String link, Boolean inVault) {}
