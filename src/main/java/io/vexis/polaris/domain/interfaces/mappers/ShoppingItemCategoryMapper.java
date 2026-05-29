package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.CategoryDTO;
import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingItemCategoryMapper {

  CategoryDTO toDTO(ShoppingItemCategory shoppingItemCategory);
}
