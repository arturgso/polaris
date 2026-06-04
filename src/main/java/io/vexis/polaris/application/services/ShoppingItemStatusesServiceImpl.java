package io.vexis.polaris.application.services;

import io.vexis.polaris.domain.interfaces.mappers.ShoppingItemStatusMapper;
import io.vexis.polaris.domain.interfaces.repositories.ShoppingItemStatusesRepository;
import io.vexis.polaris.domain.interfaces.services.ShoppingItemStatusesService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.statuses.ShoppingItemStatusDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemStatus;
import io.vexis.polaris.shared.ListMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingItemStatusesServiceImpl implements ShoppingItemStatusesService {

  private final ShoppingItemStatusesRepository repository;
  private final ShoppingItemStatusMapper mapper;

  @Override
  public List<ShoppingItemStatusDTO> list() {
    log.debug("Listing shopping item statuses");
    List<ShoppingItemStatus> shoppingItemStatusList = repository.findAll();
    return ListMapper.createResponseList(shoppingItemStatusList, mapper::toDTO);
  }

  @Override
  public ShoppingItemStatus getEntity(Long statusId) {
    log.debug("Loading shopping item status id={}", statusId);
    return repository.getReferenceById(statusId);
  }
}
