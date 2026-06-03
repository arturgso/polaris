package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.ShoppingItemStatusesService;
import io.vexis.polaris.domain.models.dtos.shoppinglist.statuses.ShoppingItemStatusDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-item-statuses")
@RequiredArgsConstructor
public class ShoppingItemStatusesController {

  private final ShoppingItemStatusesService service;

  @GetMapping
  public ResponseEntity<List<ShoppingItemStatusDTO>> list() {
    return ResponseEntity.ok(service.list());
  }
}
