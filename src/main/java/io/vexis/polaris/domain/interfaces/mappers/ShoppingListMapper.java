package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.entities.ShoppingList;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {ShoppingItemMapper.class})
public interface ShoppingListMapper {

  ShoppingListDTO toDTO(ShoppingList shoppingList);
}
