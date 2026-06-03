package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.shoppinglist.statuses.ShoppingItemStatusDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemStatus;
import java.util.List;

public interface ShoppingItemStatusesService {

  List<ShoppingItemStatusDTO> list();

  ShoppingItemStatus getEntity(Long statusId);
}
