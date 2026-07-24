package io.vexis.polaris.domain.models.dtos.filters;

public record ShoppingItemFiltersDTO(
    String status, String tag, String title, Long listId, Boolean inVault) {}
