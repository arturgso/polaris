package io.vexis.polaris.domain.interfaces.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.NewGiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {GiftsMapper.class}
)
public interface GiftListMapper {

    GiftList toEntity(NewGiftListDTO dto);
    GiftListDTO toDTO(GiftList giftList);
}