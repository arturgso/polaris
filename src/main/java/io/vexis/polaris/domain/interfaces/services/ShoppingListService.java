package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListBasicInfoDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import io.vexis.polaris.shared.dtos.NewListDTO;
import java.util.List;

public interface ShoppingListService
    extends FilteredCrudService<
        NewListDTO, NewListDTO, ShoppingListDTO, ShoppingList, Long, ListEntityFiltersDTO> {
  ShoppingListDTO getById(Long id);

  List<ShoppingListBasicInfoDTO> listBasicInfo();

  void moveToVault(Long id);

  void delete(Long id, boolean deleteItems, String vaultPassword);
}
