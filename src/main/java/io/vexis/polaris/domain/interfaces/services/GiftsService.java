package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import java.util.List;

public interface GiftsService {
  GiftDTO create(NewGiftDTO dto);

  List<GiftDTO> list(GiftFiltersDTO filters);

  List<GiftDTO> listByPerson(GiftFiltersDTO filtersDTO);

  void update(UpdateGiftDTO dto, Long id);

  void delete(Long id);
}
