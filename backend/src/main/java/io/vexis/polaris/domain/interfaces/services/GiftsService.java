package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.entities.Gift;

import java.util.UUID;

public interface GiftsService {
    GiftDTO create(NewGiftDTO dto);
    GiftDTO get(UUID personId);
}
