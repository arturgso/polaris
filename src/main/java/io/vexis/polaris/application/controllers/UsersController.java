package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.UserService;
import io.vexis.polaris.domain.models.dtos.users.NewUserDTO;
import io.vexis.polaris.domain.models.dtos.users.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

  private final UserService service;

  @PostMapping
  public ResponseEntity<UserDTO> create(@RequestBody @Valid NewUserDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }
}
