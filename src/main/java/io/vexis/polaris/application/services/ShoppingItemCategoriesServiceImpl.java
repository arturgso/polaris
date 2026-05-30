package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.ShoppingItemCategoryFactory;
import io.vexis.polaris.domain.exceptions.ShoppingItemCategoryNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemCategoryMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemCategoriesRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.CategoryDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.NewCategoryDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingItemCategoriesServiceImpl implements ShoppingItemCategoriesService {

  private final ShoppingItemCategoriesRepository repository;
  private final ShoppingItemCategoryFactory factory;
  private final ShoppingItemCategoryMapper mapper;

  @Override
  public CategoryDTO create(NewCategoryDTO dto) {
    var shoppingItemCategory = repository.save(factory.create(dto.name(), dto.color()));
    return mapper.toDTO(shoppingItemCategory);
  }

  @Override
  public List<CategoryDTO> getAll() {
    List<ShoppingItemCategory> shoppingItemCategoryList = repository.findAll();
    List<CategoryDTO> response = new ArrayList<>();

    for (ShoppingItemCategory shoppingItemCategory : shoppingItemCategoryList) {
      response.add(mapper.toDTO(shoppingItemCategory));
    }

    return response;
  }

  @Override
  public ShoppingItemCategory getEntity(Long id) {
    return repository
        .findById(id)
        .orElseThrow(ShoppingItemCategoryNotFoundException::new);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
