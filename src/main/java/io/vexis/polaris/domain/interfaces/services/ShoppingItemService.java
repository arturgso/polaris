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
  ShoppingItemDTO create(NewShoppingItemDTO dto, String vaultPassword);

  List<ShoppingItemDTO> listRecent();

  List<ShoppingItemDTO> listAllInVault();

  Long countAll();

  BigDecimal getTotalPrice();

  void moveShoppingItemToVault(Long id);

  void moveShoppingItemsToVault(List<ShoppingItem> items);
}
