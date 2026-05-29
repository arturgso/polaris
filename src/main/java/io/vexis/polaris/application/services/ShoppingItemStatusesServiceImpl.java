package io.vexis.polaris.application.services;

import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemStatusMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemStatusesRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemStatusesService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.statuses.ShoppingItemStatusDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingItemStatusesServiceImpl implements ShoppingItemStatusesService {

  private final ShoppingItemStatusesRepository repository;
  private final ShoppingItemStatusMapper mapper;

  @Override
  public List<ShoppingItemStatusDTO> getAll() {
    List<ShoppingItemStatus> shoppingItemStatusList = repository.findAll();
    List<ShoppingItemStatusDTO> response = new ArrayList<>();

    for (ShoppingItemStatus shoppingItemStatus : shoppingItemStatusList) {
      response.add(mapper.toDTO(shoppingItemStatus));
    }

    return response;
  }

  @Override
  public ShoppingItemStatus getEntity(Long statusId) {
    return repository.getReferenceById(statusId);
  }
}
