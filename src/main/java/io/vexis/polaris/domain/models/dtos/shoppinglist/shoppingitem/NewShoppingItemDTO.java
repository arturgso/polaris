package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;

public record NewShoppingItemDTO(
    @NotBlank String title,
    String link,
    String category,
    @NotNull BigDecimal price,
    ShoppingItemStatus status) {}
