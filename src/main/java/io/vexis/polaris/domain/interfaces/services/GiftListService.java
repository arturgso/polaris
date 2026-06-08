package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;
import io.vexis.polaris.shared.dtos.NewListDTO;

public interface GiftListService
    extends ListCrudService<NewListDTO, NewListDTO, GiftListDTO, GiftList, Long> {

  GiftListDTO getById(Long id);
}
