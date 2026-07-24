package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.VaultService;
import io.vexis.polaris.domain.models.dtos.filters.GiftFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.giftlist.GiftListDTO;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.domain.models.dtos.vault.UnlockVaultRequestDTO;
import io.vexis.polaris.domain.models.dtos.vault.VaultTokenResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vault")
@RequiredArgsConstructor
public class VaultController {

  private final VaultService vaultService;

  @GetMapping("/gifts")
  public ResponseEntity<List<GiftDTO>> listGifts(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String event,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) String link,
      @RequestParam(required = false) Long giftListId) {
    return ResponseEntity.ok()
        .body(
            vaultService.listGifts(
                new GiftFiltersDTO(null, status, event, title, link, giftListId, Boolean.TRUE)));
  }

  @GetMapping("/gift-lists")
  public ResponseEntity<List<GiftListDTO>> listGiftLists(
      @RequestParam(required = false) String title) {
    return ResponseEntity.ok()
        .body(vaultService.listGiftLists(new ListEntityFiltersDTO(title, Boolean.TRUE)));
  }

  @GetMapping("/shopping-items")
  public ResponseEntity<List<ShoppingItemDTO>> listShoppingItems(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String tag,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) Long listId) {
    return ResponseEntity.ok()
        .body(
            vaultService.listShoppingItems(
                new ShoppingItemFiltersDTO(status, tag, title, listId, Boolean.TRUE)));
  }

  @GetMapping("/shopping-lists")
  public ResponseEntity<List<ShoppingListDTO>> listShoppingLists(
      @RequestParam(required = false) String title) {
    return ResponseEntity.ok()
        .body(vaultService.listShoppingLists(new ListEntityFiltersDTO(title, Boolean.TRUE)));
  }

  @PostMapping("/unlock")
  public ResponseEntity<VaultTokenResponseDTO> unlock(
      @RequestBody UnlockVaultRequestDTO request, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return ResponseEntity.ok()
        .body(new VaultTokenResponseDTO(vaultService.unlock(request.password(), userDetails)));
  }
}
