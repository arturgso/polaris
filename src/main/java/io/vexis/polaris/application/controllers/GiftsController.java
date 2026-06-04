package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gifts")
@RequiredArgsConstructor
public class GiftsController {

  private final GiftsService service;

  @PostMapping
  public ResponseEntity<GiftDTO> create(@RequestBody @Valid NewGiftDTO dto) {
    return ResponseEntity.status(201).body(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<GiftDTO>> list(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String event,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String link) {
    return ResponseEntity.ok(
        service.list(new GiftFiltersDTO(null, status, event, title, link)));
  }

  @GetMapping("by-person")
  public ResponseEntity<List<GiftDTO>> listByPerson(
      @RequestParam Long personId,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String event,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String link) {
    return ResponseEntity.ok(
        service.listByPerson(new GiftFiltersDTO(personId, status, event, title, link)));
  }

  @PatchMapping("{id}")
  public ResponseEntity<Void> update(@RequestBody UpdateGiftDTO dto, @PathVariable Long id) {
    service.update(dto, id);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.ok(null);
  }
}
