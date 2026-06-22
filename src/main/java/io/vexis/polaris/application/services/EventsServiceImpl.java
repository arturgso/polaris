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
import io.vexis.polaris.shared.ListMapper;
import io.vexis.polaris.shared.TextUtils;
import jakarta.transaction.Transactional;
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
    var event = repository.save(factory.create(dto.name(), dto.color()));
    log.info("Event created with id={}", event.getId());
    return mapper.toDTO(event);
  }

  @Override
  public List<EventDTO> list() {
    log.debug("Listing events");
    List<Event> eventList = repository.findAll();
    return ListMapper.createResponseList(eventList, mapper::toDTO);
  }

  @Override
  public Event getEntity(String tag) {
    log.debug("Loading event by tag");
    return repository
        .findByTag(tag)
        .orElseThrow(EventNotFoundException::new);

  }

  @Transactional
  @Override
  public void update(UpdateEventDTO dto, String tag) {
    log.info("Updating event tag={}", tag);
    var event = getEntity(tag);
    event = mapper.update(dto, event);

    if (dto.tag() != null) {
      event.setTag(dto.tag().toUpperCase());
    }

    if (dto.color() != null) {
      event.setColor(TextUtils.normalizeColor(dto.color()));
    }

    repository.save(event);
    log.info("Event updated tag={}", tag);
  }

  @Transactional
  @Override
  public void delete(String tag) {
    log.info("Deleting event tag={}", tag);
    repository.deleteByTag(tag);
    log.info("Event deleted tag={}", tag);
  }
}
