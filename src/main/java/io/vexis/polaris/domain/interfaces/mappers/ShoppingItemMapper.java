package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.enums.ShoppingItemStatus;
import io.vexis.polaris.domain.models.dtos.StatusDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItem;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import io.vexis.polaris.shared.TextUtils;
import org.mapstruct.*;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShoppingItemMapper {
  ShoppingItemDTO toDTO(ShoppingItem item);

  @Mapping(target = "category", ignore = true)
  ShoppingItem partialUpdate(UpdateShoppingItemDTO dto, @MappingTarget ShoppingItem item);

  default String map(ShoppingItemCategory category) {
    if (category == null || category.getTag() == null) {
      return null;
    }

    return TextUtils.normalizeTag(category.getTag());
  }

  default StatusDTO map(ShoppingItemStatus status) {
    if (status == null) {
      return null;
    }

    return new StatusDTO(status.name(), status.getColor());
  }
}
