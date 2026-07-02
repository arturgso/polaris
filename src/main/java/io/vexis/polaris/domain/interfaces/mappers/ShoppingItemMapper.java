package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import io.vexis.polaris.domain.models.dtos.StatusDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import org.mapstruct.*;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShoppingItemMapper {
  ShoppingItemDTO toDTO(ShoppingItem item);

  @Mapping(target = "category", ignore = true)
  ShoppingItem partialUpdate(UpdateShoppingItemDTO dto, @MappingTarget ShoppingItem item);

default StatusDTO map(ShoppingItemStatus status) {
    if (status == null) {
      return null;

    }

    return new StatusDTO(
        status.name(),
        status.getColor());
  }
}
