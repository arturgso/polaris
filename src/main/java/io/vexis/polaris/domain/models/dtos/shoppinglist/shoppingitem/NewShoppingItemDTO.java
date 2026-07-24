package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record NewShoppingItemDTO(
    @NotBlank String title,
    String link,
    String category,
    @NotNull BigDecimal price,
    ShoppingItemStatus status,
    Long listId) {}
