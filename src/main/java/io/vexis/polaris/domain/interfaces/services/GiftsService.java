package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import java.util.List;

public interface GiftsService {
  GiftDTO create(NewGiftDTO dto);

  List<GiftDTO> getAllFromPerson(Long personId);

  void updateGift(UpdateGiftDTO dto, Long giftId);

  void deleteGift(Long giftId);
}
