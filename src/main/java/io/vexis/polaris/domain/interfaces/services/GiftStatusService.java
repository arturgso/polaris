package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.giftstatus.GiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.NewGiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.UpdateGiftStatusDTO;
import io.vexis.polaris.domain.models.entities.GiftStatus;
import java.util.List;

public interface GiftStatusService {
  GiftStatusDTO create(NewGiftStatusDTO dto);

  List<GiftStatusDTO> list();

  GiftStatus getEntity(Long id);

  GiftStatus getEntityByName(String name);

  void update(UpdateGiftStatusDTO dto, Long id);

  void delete(Long id);
}
