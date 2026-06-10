package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.VaultService;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.dtos.vault.UnlockVaultRequestDTO;
import io.vexis.polaris.domain.models.dtos.vault.VaultTokenResponseDTO;
import io.vexis.polaris.domain.models.dtos.vault.VaultTokenValidationResponseDTO;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vault")
@RequiredArgsConstructor
public class VaultController {

  private final VaultService vaultService;

  // TODO: Require and validate a short-lived vault token for every endpoint in this controller.

  @GetMapping("/gifts")
  public ResponseEntity<List<GiftDTO>> listGifts() {
    return ResponseEntity.ok().body(vaultService.listGifts());
  }

  @GetMapping("/gift-lists")
  public ResponseEntity<List<GiftListDTO>> listGiftLists() {
    return ResponseEntity.ok().body(vaultService.listGiftLists());
  }

  @GetMapping("/shopping-items")
  public ResponseEntity<List<ShoppingItemDTO>> listShoppingItems() {
    return ResponseEntity.ok().body(vaultService.listShoppingItems());
  }

  @GetMapping("/shopping-lists")
  public ResponseEntity<List<ShoppingListDTO>> listShoppingLists() {
    return ResponseEntity.ok().body(vaultService.listShoppingLists());
  }

  @PostMapping("/unlock")
  public ResponseEntity<VaultTokenResponseDTO> unlock(@RequestBody UnlockVaultRequestDTO request, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return ResponseEntity.ok().body(new VaultTokenResponseDTO(vaultService.unlock(request.password(), userDetails)));
  }

  @PostMapping("/validate")
  public ResponseEntity<VaultTokenValidationResponseDTO> validate(
      @RequestHeader("X-Vault-Token") String token) {
    // TODO: Delegate to VaultService.validate(token).
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
