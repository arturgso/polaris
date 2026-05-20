package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @PatchMapping("{id}")
    public ResponseEntity<Void> update(@RequestBody UpdatePersonDTO dto, @PathVariable UUID id) {
        service.update(dto, id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok().body(null);
    }
}
