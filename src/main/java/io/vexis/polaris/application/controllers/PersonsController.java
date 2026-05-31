package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonsController {

  private final PersonsService service;

  @PostMapping
  public ResponseEntity<PersonDTO> create(@RequestBody @Valid NewPersonDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<PersonDTO>> getAll() {
    return ResponseEntity.ok(service.getAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<PersonDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @PatchMapping("{id}")
  public ResponseEntity<Void> update(@RequestBody UpdatePersonDTO dto, @PathVariable Long id) {
    service.update(dto, id);
    return ResponseEntity.ok().body(null);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.ok().body(null);
  }
}
