package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.EventsFactory;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

  private final EventsRepository repository;
  private final EventsFactory factory;
  private final EventsMapper mapper;

  @Override
  public EventDTO create(NewEventDTO dto) {
    var event = repository.save(factory.create(dto.name()));
    return mapper.toDTO(event);
  }

  @Override
  public List<EventDTO> getAll() {
    List<Event> eventList = repository.findAll();
    List<EventDTO> response = new ArrayList<>();

    for (Event event : eventList) {
      response.add(mapper.toDTO(event));
    }

    return response;
  }

  @Override
  public Event getEntity(Long id) {
    return repository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
  }

  @Override
  public Event getEntityByName(String name) {
    return repository
        .findByName(factory.normalizeName(name))
        .orElseThrow(() -> new RuntimeException("Event not found"));
  }

  @Transactional
  @Override
  public void update(UpdateEventDTO dto, Long id) {
    var event = repository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    event = mapper.update(dto, event);

    if (dto.name() != null) {
      event.setName(factory.normalizeName(dto.name()));
    }

    repository.save(event);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
