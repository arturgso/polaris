package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.GiftListService;
import io.vexis.polaris.domain.interfaces.services.GiftsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vault")
@RequiredArgsConstructor
public class VaultController {

  private final GiftsService giftsService;
  private final GiftListService giftListService;

  // TODO: Require and validate a short-lived vault token for every endpoint in this controller.
  // TODO: Add the unlock endpoint responsible for issuing the vault token.
  // TODO: Add endpoints for listing and retrieving vault content.
  // TODO: Add equivalent operations for shopping items and shopping lists.

  @PatchMapping("/gifts/{id}")
  public ResponseEntity<Void> moveGiftToVault(@PathVariable Long id) {
    giftsService.moveGiftToVault(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/gift-lists/{id}")
  public ResponseEntity<Void> moveGiftListToVault(@PathVariable Long id) {
    giftListService.moveToVault(id);
    return ResponseEntity.noContent().build();
  }
}
