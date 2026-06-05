package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.NewGiftListDTO;
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
@RequestMapping("/gift-lists")
@RequiredArgsConstructor
public class GiftListController {

  private final GiftListService service;

  @PostMapping
  public ResponseEntity<GiftListDTO> create(@RequestBody NewGiftListDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  @GetMapping("{id}")
  public ResponseEntity<GiftListDTO> getById(@PathVariable Long id) {
    return null;
  }

  @PatchMapping("{id}")
  public ResponseEntity<Void> update(@RequestBody NewGiftListDTO dto, @PathVariable Long id) {
    return null;
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    return null;
  }
}
