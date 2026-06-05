package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.NewGiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;

public interface GiftListService
    extends ListCrudService<NewGiftListDTO, NewGiftListDTO, GiftListDTO, GiftList, Long> {

  GiftListDTO getById(Long id);
}
