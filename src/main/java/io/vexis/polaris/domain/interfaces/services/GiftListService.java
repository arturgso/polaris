package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.entities.GiftList;
import io.vexis.polaris.shared.dtos.NewListDTO;

public interface GiftListService
    extends FilteredCrudService<
        NewListDTO, NewListDTO, GiftListDTO, GiftList, Long, ListEntityFiltersDTO> {

  GiftListDTO getById(Long id);

  void moveToVault(Long id);
}
