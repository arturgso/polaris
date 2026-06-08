package io.vexis.polaris.domain.interfaces.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GiftsMapper {

  @Mapping(target = "giftFor", source = "giftFor.name")
  @Mapping(target = "event", source = "event.tag")
  @Mapping(target = "status", source = "status.tag")
  GiftDTO toDTO(Gift gift);

  @Mapping(target = "giftFor", ignore = true)
  @Mapping(target = "giftList", ignore = true)
  @Mapping(target = "event", ignore = true)
  @Mapping(target = "status", ignore = true)
  Gift update(UpdateGiftDTO update, @MappingTarget Gift gift);
}
