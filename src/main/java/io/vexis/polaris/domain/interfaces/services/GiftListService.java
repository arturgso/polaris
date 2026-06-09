package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;
import io.vexis.polaris.shared.dtos.NewListDTO;

import java.util.List;

public interface GiftListService
    extends ListCrudService<NewListDTO, NewListDTO, GiftListDTO, GiftList, Long> {

  GiftListDTO getById(Long id);

  List<GiftListDTO> listAllInVault();

  void moveToVault(Long id);
}
