package io.vexis.polaris.application.services;

import io.vexis.polaris.domain.exceptions.ShoppingListNotFoundException;
import io.vexis.polaris.domain.exceptions.VaultAccessDeniedException;
import io.vexis.polaris.application.security.VaultPasswordValidator;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingListMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemRepository;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingListRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import io.vexis.polaris.domain.specs.ListEntitySpec;
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import io.vexis.polaris.shared.dtos.NewListDTO;
import io.vexis.polaris.shared.utils.EntityUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingListServiceImpl implements ShoppingListService {

  private final ShoppingListMapper mapper;
  private final ShoppingListRepository repository;
  private final ShoppingItemRepository shoppingItemRepository;
  private final VaultPasswordValidator vaultPasswordValidator;

  @Override
  public ShoppingListDTO create(NewListDTO dto) {
    log.info("Creating shopping list");
    var shoppingList = new ShoppingList();
    shoppingList.setTitle(TextUtils.normalizeText(dto.title()));
    shoppingList = repository.save(shoppingList);

    log.info("Shopping list created with id={}", shoppingList.getId());
    return mapper.toDTO(shoppingList);
  }

  @Override
  public ShoppingList getEntity(Long id) {
    log.debug("Loading shopping list id={}", id);
    return repository.findById(id).orElseThrow(ShoppingListNotFoundException::new);
  }

  @Override
  public ShoppingListDTO getById(Long id) {
    log.debug("Loading shopping list DTO id={}", id);

    var list = getEntity(id);

    if (Boolean.TRUE.equals(list.getInVault())) {
      throw new VaultAccessDeniedException("Vault access denied");
    }

    return mapper.toDTO(list);
  }

  @Override
  @Transactional
  public void moveToVault(Long id) {
    var list = getEntity(id);
    list.setInVault(true);
    repository.save(list);
    list.getItems().forEach(item -> item.setInVault(true));
    shoppingItemRepository.saveAll(list.getItems());
  }

  @Transactional
  @Override
  public void update(NewListDTO dto, Long id) {
    update(dto, id, null);
  }

  @Transactional
  @Override
  public void update(NewListDTO dto, Long id, String vaultPassword) {
    log.info("Updating shopping list id={}", id);
    var shoppingList = EntityUtils.findOrThrow(repository, id);
    if (Boolean.TRUE.equals(shoppingList.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    if (dto.title() != null) {
      shoppingList.setTitle(TextUtils.normalizeText(dto.title()));
    }

    repository.save(shoppingList);
    log.info("Shopping list updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    delete(id, null);
  }

  @Transactional
  @Override
  public void delete(Long id, String vaultPassword) {
    log.info("Deleting shopping list id={}", id);
    if (!repository.existsById(id)) {
      throw new ShoppingListNotFoundException();
    }
    var shoppingList = EntityUtils.findOrThrow(repository, id);
    if (Boolean.TRUE.equals(shoppingList.getInVault())) {
      vaultPasswordValidator.validate(vaultPassword);
    }
    repository.deleteById(id);
    log.info("Shopping list deleted id={}", id);
  }

  @Override
  public List<ShoppingListDTO> list(ListEntityFiltersDTO filters) {
    log.debug("Listing shopping lists");
    List<ShoppingList> lists = repository.findAll(ListEntitySpec.byFilters(filters));
    return ListMapper.createResponseList(lists, mapper::toDTO);
  }
}
