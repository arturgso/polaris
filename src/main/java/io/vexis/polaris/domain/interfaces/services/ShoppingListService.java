package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import io.vexis.polaris.shared.dtos.NewListDTO;

public interface ShoppingListService
    extends ListCrudService<NewListDTO, NewListDTO, ShoppingListDTO, ShoppingList, Long> {

  ShoppingListDTO getById(Long id);

  void moveToVault(Long id);
}
