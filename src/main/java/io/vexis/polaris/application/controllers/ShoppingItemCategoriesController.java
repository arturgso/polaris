package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.ShoppingItemCategoriesService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.CategoryDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.categories.NewCategoryDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-item-categories")
@RequiredArgsConstructor
public class ShoppingItemCategoriesController {

  private final ShoppingItemCategoriesService service;

  @PostMapping
  public ResponseEntity<CategoryDTO> create(@RequestBody @Valid NewCategoryDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> list() {
    return ResponseEntity.ok(service.list());
  }

  @DeleteMapping("{tag}")
  public ResponseEntity<Void> delete(@PathVariable String tag) {
    service.delete(tag);
    return ResponseEntity.ok().body(null);
  }
}
