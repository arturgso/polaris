package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.EventsFactory;
import io.vexis.polaris.domain.exceptions.EventNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.EventsMapper;
import io.vexis.polaris.domain.interfaces.repositories.EventsRepository;
import io.vexis.polaris.domain.interfaces.services.EventsService;
import io.vexis.polaris.domain.models.dtos.events.EventDTO;
import io.vexis.polaris.domain.models.dtos.events.NewEventDTO;
import io.vexis.polaris.domain.models.dtos.events.UpdateEventDTO;
import io.vexis.polaris.domain.models.entities.Event;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventsServiceImpl implements EventsService {

  private final EventsRepository repository;
  private final EventsFactory factory;
  private final EventsMapper mapper;

  @Override
  public EventDTO create(NewEventDTO dto) {
    log.info("Creating event");
    var event = repository.save(factory.create(dto.tag(), dto.name(), dto.color()));
    log.info("Event created with id={}", event.getId());
    return mapper.toDTO(event);
  }

  @Override
  public List<EventDTO> list() {
    log.debug("Listing events");
    List<Event> eventList = repository.findAll();
    List<EventDTO> response = new ArrayList<>();

    for (Event event : eventList) {
      response.add(mapper.toDTO(event));
    }

    log.debug("Found {} events", response.size());
    return response;
  }

  @Override
  public Event getEntity(Long id) {
    log.debug("Loading event id={}", id);
    return repository.findById(id).orElseThrow(EventNotFoundException::new);
  }

  @Override
  public Event getEntityByTag(String tag) {
    log.debug("Loading event by tag");
    return repository
        .findByTag(factory.normalizeTag(tag))
        .orElseThrow(EventNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdateEventDTO dto, Long id) {
    log.info("Updating event id={}", id);
    var event = repository.findById(id).orElseThrow(EventNotFoundException::new);
    event = mapper.update(dto, event);

    if (dto.tag() != null) {
      event.setTag(factory.normalizeTag(dto.tag()));
    }

    if (dto.color() != null) {
      event.setColor(factory.normalizeColor(dto.color()));
    }

    repository.save(event);
    log.info("Event updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    log.info("Deleting event id={}", id);
    repository.deleteById(id);
    log.info("Event deleted id={}", id);
  }
}
