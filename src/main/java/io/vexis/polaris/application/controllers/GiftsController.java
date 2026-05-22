package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.UpdateGiftDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/gifts")
@RequiredArgsConstructor
public class GiftsController {

  private final GiftsService service;

  @PostMapping
  public ResponseEntity<GiftDTO> create(@RequestBody @Valid NewGiftDTO dto) {
    return ResponseEntity.status(201).body(service.create(dto));
  }

  @GetMapping("{personId}")
  public ResponseEntity<List<GiftDTO>> getAll(@PathVariable String personId) {
    return ResponseEntity.ok(service.getAllFromPerson(UUID.fromString(personId)));
  }

  @PatchMapping("{giftId}")
  public ResponseEntity<Void> update(@RequestBody UpdateGiftDTO dto, @PathVariable String giftId) {
    service.updateGift(dto, UUID.fromString(giftId));
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("{giftId}")
  public ResponseEntity<Void> delete(@PathVariable String giftId) {
    service.deleteGift(UUID.fromString(giftId));
    return ResponseEntity.ok(null);
  }
}
