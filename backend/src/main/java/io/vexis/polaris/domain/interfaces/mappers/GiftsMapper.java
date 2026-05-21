package io.vexis.polaris.domain.interfaces.mappers;

import org.mapstruct.*;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GiftsMapper {

    @Mapping(target = "giftFor", source = "giftFor.name")
    GiftDTO toDTO(Gift gift);

    @Mapping(target = "giftFor", ignore = true)
    Gift update(UpdateGiftDTO update, @MappingTarget Gift gift);
}
