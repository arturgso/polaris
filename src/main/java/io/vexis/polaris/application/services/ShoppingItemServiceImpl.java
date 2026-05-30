package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.ShoppingItemFactory;
import io.vexis.polaris.domain.exceptions.ShoppingItemNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemStatusesService;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.domain.specs.ShoppingItemsSpec;
import io.vexis.polaris.shared.TextUitls;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingItemServiceImpl implements ShoppingItemService {

  private final ShoppingItemRepository repository;
  private final ShoppingItemMapper mapper;
  private final ShoppingItemFactory factory;
  private final ShoppingItemStatusesService statusesService;
  private final ShoppingItemCategoriesService categoriesService;

  @Override
  public ShoppingItemDTO create(NewShoppingItemDTO dto) {
    var item =
        factory.create(dto.title(), dto.link(), dto.categoryId(), dto.price(), dto.statusId());

    item = repository.save(item);
    return mapper.toDTO(item);
  }

  @Override
  public List<ShoppingItemDTO> list(ShoppingItemFiltersDTO filtersDTO) {
    List<ShoppingItem> itemList = repository.findAll(ShoppingItemsSpec.byFilters(filtersDTO));
    return createResponseList(itemList);
  }

  @Override
  public List<ShoppingItemDTO> listRecently() {
    List<ShoppingItem> itemList = repository.findRecentlyInserts();
    return createResponseList(itemList);
  }

  @Override
  public Long countAll() {
    return repository.count();
  }

  @Override
  public BigDecimal getTotalPrice() {
    return repository.getTotalPrice();
  }

  @Transactional
  @Override
  public void update(UpdateShoppingItemDTO dto, Long itemId) {
    var item = repository.findById(itemId).orElseThrow(ShoppingItemNotFoundException::new);
    item = mapper.partialUpdate(dto, item);

    if (dto.title() != null) {
      item.setTitle(TextUitls.normalizeText(dto.title()));
    }

    if (dto.categoryId() != null) {
      item.setCategory(categoriesService.getEntity(dto.categoryId()));
    }

    if (dto.statusId() != null) {
      item.setStatus(statusesService.getEntity(dto.statusId()));
    }

    repository.save(item);
  }

  @Transactional
  @Override
  public void delete(Long itemId) {
    var item = repository.findById(itemId).orElseThrow(ShoppingItemNotFoundException::new);
    repository.delete(item);
  }

  private List<ShoppingItemDTO> createResponseList(List<ShoppingItem> shoppingItems) {

    List<ShoppingItemDTO> responseList = new ArrayList<>();

    for (ShoppingItem item : shoppingItems) {
      responseList.add(mapper.toDTO(item));
    }

    return responseList;
  }
}
