package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.events.EventDTO;
import io.vexis.polaris.domain.models.dtos.events.NewEventDTO;
import io.vexis.polaris.domain.models.dtos.events.UpdateEventDTO;
import io.vexis.polaris.domain.models.entities.Event;
import java.util.List;

public interface EventsService {
  EventDTO create(NewEventDTO dto);

  List<EventDTO> list();

  Event getEntity(Long id);

  Event getEntityByName(String name);

  void update(UpdateEventDTO dto, Long id);

  void delete(Long id);
}
