package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import java.math.BigDecimal;

public record UpdateShoppingItemDTO(
    String title, String link, Long categoryId, BigDecimal price, Long statusId, Long shoppingListId) {}
