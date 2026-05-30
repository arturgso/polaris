package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import java.util.List;

public interface ShoppingItemService {
  ShoppingItemDTO create(NewShoppingItemDTO dto);

  List<ShoppingItemDTO> list(ShoppingItemFiltersDTO filtersDTO);

  void update(UpdateShoppingItemDTO dto, Long itemId);

  void delete(Long itemId);
}
