package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = ShoppingItemStatusMapper.class)
public interface ShoppingItemMapper {
  ShoppingItemDTO toDTO(ShoppingItem item);

  ShoppingItem partialUpdate(UpdateShoppingItemDTO dto, @MappingTarget ShoppingItem item);
}
