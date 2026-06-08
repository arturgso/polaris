package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import java.math.BigDecimal;
import java.util.List;

public interface ShoppingItemService
    extends FilteredCrudService<
        NewShoppingItemDTO,
        UpdateShoppingItemDTO,
        ShoppingItemDTO,
        ShoppingItem,
        Long,
        ShoppingItemFiltersDTO> {
  List<ShoppingItemDTO> listRecent();

  Long countAll();

  BigDecimal getTotalPrice();
}
