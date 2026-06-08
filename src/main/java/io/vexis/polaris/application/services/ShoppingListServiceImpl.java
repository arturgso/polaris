package io.vexis.polaris.application.services;

import io.vexis.polaris.domain.exceptions.ShoppingListNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.ShoppingListMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingListRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import io.vexis.polaris.shared.dtos.NewListDTO;
import io.vexis.polaris.shared.utils.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListServiceImpl implements ShoppingListService {

  private final ShoppingListMapper mapper;
  private final ShoppingListRepository repository;

  @Override
  public ShoppingListDTO create(NewListDTO dto) {
    var shoppingList = new ShoppingList();
    shoppingList.setTitle(TextUtils.normalizeText(dto.title()));
    shoppingList = repository.save(shoppingList);

    return mapper.toDTO(shoppingList);
  }

  @Override
  public ShoppingList getEntity(Long id) {
    return repository.findById(id).orElseThrow(ShoppingListNotFoundException::new);
  }

  @Override
  public ShoppingListDTO getById(Long id) {
    return mapper.toDTO(getEntity(id));
  }

  @Transactional
  @Override
  public void update(NewListDTO dto, Long id) {
    var shoppingList = EntityUtils.findOrThrow(repository, id);
    if (dto.title() != null) {
      shoppingList.setTitle(TextUtils.normalizeText(dto.title()));
    }

    repository.save(shoppingList);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    if (!repository.existsById(id)) {
      throw new ShoppingListNotFoundException();
    }
    repository.deleteById(id);
  }

  @Override
  public List<ShoppingListDTO> list() {
    var lists = repository.findAll();
    return ListMapper.createResponseList(lists, mapper::toDTO);
  }

}
