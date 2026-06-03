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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingItemCategoriesServiceImpl implements ShoppingItemCategoriesService {

  private final ShoppingItemCategoriesRepository repository;
  private final ShoppingItemCategoryFactory factory;
  private final ShoppingItemCategoryMapper mapper;

  @Override
  public CategoryDTO create(NewCategoryDTO dto) {
    log.info("Creating shopping item category");
    var shoppingItemCategory = repository.save(factory.create(dto.name(), dto.color()));
    log.info("Shopping item category created with id={}", shoppingItemCategory.getId());
    return mapper.toDTO(shoppingItemCategory);
  }

  @Override
  public List<CategoryDTO> list() {
    log.debug("Listing shopping item categories");
    List<ShoppingItemCategory> shoppingItemCategoryList = repository.findAll();
    List<CategoryDTO> response = new ArrayList<>();

    for (ShoppingItemCategory shoppingItemCategory : shoppingItemCategoryList) {
      response.add(mapper.toDTO(shoppingItemCategory));
    }

    log.debug("Found {} shopping item categories", response.size());
    return response;
  }

  @Override
  public ShoppingItemCategory getEntity(Long id) {
    log.debug("Loading shopping item category id={}", id);
    return repository.findById(id).orElseThrow(ShoppingItemCategoryNotFoundException::new);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    log.info("Deleting shopping item category id={}", id);
    repository.deleteById(id);
    log.info("Shopping item category deleted id={}", id);
  }
}
