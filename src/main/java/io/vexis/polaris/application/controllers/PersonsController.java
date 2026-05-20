package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("persons")
@RequiredArgsConstructor
public class PersonsController {

    private final PersonsService service;

    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody @Valid NewPersonDTO dto) {
       return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }
}
