package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.events.EventDTO;
import io.vexis.polaris.domain.models.dtos.events.NewEventDTO;
import io.vexis.polaris.domain.models.dtos.events.UpdateEventDTO;
import io.vexis.polaris.domain.models.entities.Event;

public interface EventsService
    extends ListCrudService<NewEventDTO, UpdateEventDTO, EventDTO, Event, String> {}
