package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record NewShoppingItemDTO(
    @NotBlank String title,
    @NotBlank String link,
    Long categoryId,
    @NotNull BigDecimal price,
    Long statusId
) {
}
