package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.models.dtos.filters.ShoppingItemFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.NewShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.UpdateShoppingItemDTO;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-items")
@RequiredArgsConstructor
public class ShoppingItemsController {

  private final ShoppingItemService service;

  @PostMapping
  public ResponseEntity<ShoppingItemDTO> create(@RequestBody @Valid NewShoppingItemDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<ShoppingItemDTO>> list(
      @RequestParam(required = false) Long statusId,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String title) {
    return ResponseEntity.ok(service.list(new ShoppingItemFiltersDTO(statusId, categoryId, title)));
  }

  @PatchMapping("{itemId}")
  public ResponseEntity<Void> update(
      @RequestBody UpdateShoppingItemDTO dto, @PathVariable Long itemId) {
    service.update(dto, itemId);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("{itemId}")
  public ResponseEntity<Void> delete(@PathVariable Long itemId) {
    service.delete(itemId);
    return ResponseEntity.ok(null);
  }
}
