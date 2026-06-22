package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.EventsService;
import io.vexis.polaris.domain.models.dtos.events.EventDTO;
import io.vexis.polaris.domain.models.dtos.events.NewEventDTO;
import io.vexis.polaris.domain.models.dtos.events.UpdateEventDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {

  private final EventsService service;

  @PostMapping
  public ResponseEntity<EventDTO> create(@RequestBody @Valid NewEventDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<EventDTO>> list() {
    return ResponseEntity.ok(service.list());
  }

  @PatchMapping("{tag}")
  public ResponseEntity<Void> update(@RequestBody UpdateEventDTO dto, @PathVariable String tag) {
    service.update(dto, tag);
    return ResponseEntity.ok().body(null);
  }

  @DeleteMapping("{tag}")
  public ResponseEntity<Void> delete(@PathVariable String tag) {
    service.delete(tag);
    return ResponseEntity.ok().body(null);
  }
}
