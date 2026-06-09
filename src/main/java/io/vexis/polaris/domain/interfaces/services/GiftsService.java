package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;
import java.math.BigDecimal;
import java.util.List;

public interface GiftsService
    extends FilteredCrudService<NewGiftDTO, UpdateGiftDTO, GiftDTO, Gift, Long, GiftFiltersDTO> {
  List<GiftDTO> listByPerson(GiftFiltersDTO filtersDTO);

  Long countAll();

  BigDecimal getTotalPrice();

  void moveGiftToVault(Long id);
}
