package io.vexis.polaris.domain.models.dtos.shoppinglist.categories;

import jakarta.validation.constraints.NotBlank;

public record NewCategoryDTO(
    @NotBlank String tag
) {}
