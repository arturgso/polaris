package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import org.mapstruct.*;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = ShoppingItemStatusMapper.class,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShoppingItemMapper {
  ShoppingItemDTO toDTO(ShoppingItem item);

  @Mapping(target = "category", ignore = true)
  @Mapping(target = "status", ignore = true)
  ShoppingItem partialUpdate(UpdateShoppingItemDTO dto, @MappingTarget ShoppingItem item);
}
