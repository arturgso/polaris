package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;

import java.util.List;
import java.util.UUID;

public interface GiftsService {
    GiftDTO create(NewGiftDTO dto);
    List<GiftDTO> getAllFromPerson(UUID personId);
    void updateGift(UUID giftId);
    void deleteGift(UUID giftId);
}
