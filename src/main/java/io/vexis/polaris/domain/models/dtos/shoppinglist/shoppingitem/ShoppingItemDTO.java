package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import io.vexis.polaris.domain.models.dtos.StatusDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListBasicInfoDTO;
import java.math.BigDecimal;
import java.time.Instant;

public record ShoppingItemDTO(
    Long id,
    String title,
    String link,
    String category,
    BigDecimal price,
    StatusDTO status,
    Instant createdAt,
    Instant updatedAt,
    ShoppingListBasicInfoDTO shoppingList) {}
