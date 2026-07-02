package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.ShoppingItemCategoryFactory;
import io.vexis.polaris.domain.exceptions.ShoppingItemCategoryNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemCategoryMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemCategoriesRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.CategoryDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.NewCategoryDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.shared.ListMapper;
import jakarta.transaction.Transactional;
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
    var shoppingItemCategory = repository.save(factory.create(dto.tag()));
    log.info("Shopping item category created with id={}", shoppingItemCategory.getId());
    return mapper.toDTO(shoppingItemCategory);
  }

  @Override
  public List<CategoryDTO> list() {
    log.debug("Listing shopping item categories");
    List<ShoppingItemCategory> shoppingItemCategoryList = repository.findAll();
    return ListMapper.createResponseList(shoppingItemCategoryList, mapper::toDTO);
  }

  @Override
  public ShoppingItemCategory getEntity(String tag) {
    log.debug("Loading shopping item category tag={}", tag);
    return repository.findByTag(tag).orElseThrow(ShoppingItemCategoryNotFoundException::new);
  }

  @Transactional
  @Override
  public void delete(String tag) {
    log.info("Deleting shopping item category tag={}", tag);
    repository.deleteByTag(tag);
    log.info("Shopping item category deleted tag={}", tag);
  }
}
