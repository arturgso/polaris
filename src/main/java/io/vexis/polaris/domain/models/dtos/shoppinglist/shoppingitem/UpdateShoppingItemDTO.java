package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import java.math.BigDecimal;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;

public record UpdateShoppingItemDTO(
    String title,
    String link,
    String category,
    BigDecimal price,
    ShoppingItemStatus status,
    Long shoppingListId) {}
