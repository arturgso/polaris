package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.ShoppingItemFactory;
import io.vexis.polaris.application.security.VaultPasswordValidator;
import io.vexis.polaris.domain.exceptions.ShoppingItemNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import io.vexis.polaris.domain.specs.ShoppingItemsSpec;
import io.vexis.polaris.shared.ListConstants;
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingItemServiceImpl implements ShoppingItemService {

  private final ShoppingItemRepository repository;
  private final ShoppingItemMapper mapper;
  private final ShoppingItemFactory factory;
  private final ShoppingItemCategoriesService categoriesService;
  private final VaultPasswordValidator vaultPasswordValidator;

  private final ShoppingListService shoppingListService;

  @Override
  public ShoppingItemDTO create(NewShoppingItemDTO dto) {
    return create(dto, null);
  }

  @Override
  public ShoppingItemDTO create(NewShoppingItemDTO dto, String vaultPassword) {
    log.info(
        "Creating shopping item with categoryId={} and status={}", dto.category(), dto.status());
    var item = factory.create(dto.title(), dto.link(), dto.category(), dto.price(), dto.status());
    ShoppingList shoppingList = resolveShoppingList(dto.listId(), vaultPassword);
    if (shoppingList != null) {
      item.setShoppingList(shoppingList);
      item.setInVault(shoppingList.getInVault());
    }

    item = repository.save(item);
    log.info("Shopping item created with id={}", item.getId());
    return mapper.toDTO(item);
  }

  @Override
  public List<ShoppingItemDTO> list(ShoppingItemFiltersDTO filtersDTO) {
    log.debug(
        "Listing shopping items with filters status={}, categoryId={}, titlePresent={}",
        filtersDTO.status(),
        filtersDTO.tag(),
        filtersDTO.title() != null);
    List<ShoppingItem> itemList = repository.findAll(ShoppingItemsSpec.byFilters(filtersDTO));
    log.debug("Found {} shopping items", itemList.size());
    return ListMapper.createResponseList(itemList, mapper::toDTO);
  }

  @Override
  public List<ShoppingItemDTO> listRecent() {
    log.debug("Listing recent shopping items");
    List<ShoppingItem> itemList = repository.findRecentlyInserted();
    log.debug("Found {} recent shopping items", itemList.size());
    return ListMapper.createResponseList(itemList, mapper::toDTO);
  }

  @Override
  public Long countAll() {
    Long total = repository.count();
    log.debug("Counted {} shopping items", total);
    return total;
  }

  @Override
  public BigDecimal getTotalPrice() {
    BigDecimal totalPrice = repository.getTotalPrice();
    log.debug("Calculated shopping items total price={}", totalPrice);
    return totalPrice;
  }

  @Override
  public ShoppingItem getEntity(Long id) {
    return repository.findById(id).orElseThrow(ShoppingItemNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdateShoppingItemDTO dto, Long id) {
    update(dto, id, null);
  }

  @Transactional
  @Override
  public void update(UpdateShoppingItemDTO dto, Long id, String vaultPassword) {
    log.info("Updating shopping item id={}", id);
    var item = getEntity(id);
    if (Boolean.TRUE.equals(item.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    item = mapper.partialUpdate(dto, item);

    if (dto.title() != null) {
      item.setTitle(TextUtils.normalizeText(dto.title()));
    }

    if (dto.category() != null) {
      item.setCategory(categoriesService.getEntity(dto.category()));
    }

    if (ListConstants.NO_LIST_ID.equals(dto.listId())) {
      item.setShoppingList(null);
    } else if (dto.listId() != null) {
      ShoppingList shoppingList = resolveShoppingList(dto.listId(), vaultPassword);
      item.setShoppingList(shoppingList);
      item.setInVault(shoppingList.getInVault());
    }

    repository.save(item);
    log.info("Shopping item updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    delete(id, null);
  }

  @Transactional
  @Override
  public void delete(Long id, String vaultPassword) {
    log.info("Deleting shopping item id={}", id);
    var item = getEntity(id);
    if (Boolean.TRUE.equals(item.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    repository.delete(item);
    log.info("Shopping item deleted id={}", id);
  }

  @Override
  @Transactional
  public void moveShoppingItemToVault(Long id) {
    var item = getEntity(id);
    item.setInVault(true);
    repository.save(item);
  }

  @Override
  @Transactional
  public void moveShoppingItemsToVault(List<ShoppingItem> items) {
    items.forEach(item -> item.setInVault(true));
    repository.saveAll(items);
  }

  @Override
  public List<ShoppingItemDTO> listAllInVault() {
    return repository.findAllByInVaultTrue().stream().map(mapper::toDTO).toList();
  }

  private ShoppingList resolveShoppingList(Long listId, String vaultPassword) {
    if (listId == null || ListConstants.NO_LIST_ID.equals(listId)) {
      return null;
    }

    ShoppingList shoppingList = shoppingListService.getEntity(listId);
    if (Boolean.TRUE.equals(shoppingList.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    return shoppingList;
  }
}
