package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.CategoryDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.NewCategoryDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import java.util.List;

public interface ShoppingItemCategoriesService {

  CategoryDTO create(NewCategoryDTO dto);

  List<CategoryDTO> getAll();

  ShoppingItemCategory getEntity(Long id);

  void delete(Long id);
}
