package io.vexis.polaris.domain.models.dtos.filters;

public record ShoppingItemFiltersDTO(
        Long statusId,
        Long categoryId,
        String title
) {
}
