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
      @RequestParam(required = false) Long statusId,
      @RequestParam(required = false) Long eventId,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String link) {
    return ResponseEntity.ok(
        service.list(new GiftFiltersDTO(null, statusId, eventId, title, link)));
  }

  @GetMapping("{personId}")
  public ResponseEntity<List<GiftDTO>> getAll(
      @PathVariable Long personId,
      @RequestParam(required = false) Long statusId,
      @RequestParam(required = false) Long eventId,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String link) {
    return ResponseEntity.ok(
        service.listByPerson(new GiftFiltersDTO(personId, statusId, eventId, title, link)));
  }

  @PatchMapping("{giftId}")
  public ResponseEntity<Void> update(@RequestBody UpdateGiftDTO dto, @PathVariable Long giftId) {
    service.updateGift(dto, giftId);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("{giftId}")
  public ResponseEntity<Void> delete(@PathVariable Long giftId) {
    service.deleteGift(giftId);
    return ResponseEntity.ok(null);
  }
}
