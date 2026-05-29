package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem;

import io.vexis.polaris.domain.models.dtos.shoppinglist.statuses.ShoppingItemStatusDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.domain.models.entities.ShoppingItemStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record ShoppingItemDTO(
    Long id,
    String title,
    String link,
    ShoppingItemCategory category,
    BigDecimal price,
    ShoppingItemStatusDTO status,
    Instant createdAt,
    Instant updatedAt
) {
}
