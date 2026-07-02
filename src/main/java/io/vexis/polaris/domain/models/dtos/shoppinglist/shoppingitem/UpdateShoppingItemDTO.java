package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import java.math.BigDecimal;

public record UpdateShoppingItemDTO(
    String title,
    String link,
    String category,
    BigDecimal price,
    ShoppingItemStatus status,
    Long shoppingListId) {}
