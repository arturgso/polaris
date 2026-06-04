package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.giftstatus.GiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.NewGiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.UpdateGiftStatusDTO;
import io.vexis.polaris.domain.models.entities.GiftStatus;

public interface GiftStatusService
    extends ListCrudService<
        NewGiftStatusDTO, UpdateGiftStatusDTO, GiftStatusDTO, GiftStatus, Long> {
  GiftStatus getEntityByTag(String tag);
}
