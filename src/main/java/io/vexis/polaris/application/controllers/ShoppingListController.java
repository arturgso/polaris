package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.ShoppingListService;
import io.vexis.polaris.domain.models.dtos.filters.ListEntityFiltersDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist.ShoppingListDTO;
import io.vexis.polaris.shared.dtos.NewListDTO;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-lists")
@RequiredArgsConstructor
public class ShoppingListController {

  private final ShoppingListService service;

  @PostMapping
  public ResponseEntity<ShoppingListDTO> create(@RequestBody NewListDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<ShoppingListDTO>> getAll(
      @RequestParam(required = false) String title) {
    return ResponseEntity.ok(service.list(new ListEntityFiltersDTO(title, Boolean.FALSE)));
  }

  @GetMapping("{id}")
  public ResponseEntity<ShoppingListDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @PatchMapping("{id}")
  public ResponseEntity<Void> update(
      @RequestBody NewListDTO dto,
      @PathVariable Long id,
      @RequestHeader(value = "X-Vault-Password", required = false) String vaultPassword) {
    service.update(dto, id, vaultPassword);
    return ResponseEntity.ok().build();
  }

  @PatchMapping("{id}/vault")
  public ResponseEntity<Void> moveToVault(@PathVariable Long id) {
    service.moveToVault(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id,
      @RequestHeader(value = "X-Vault-Password", required = false) String vaultPassword) {
    service.delete(id, vaultPassword);
    return ResponseEntity.ok().build();
  }
}
