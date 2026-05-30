package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.interfaces.services.ShoppingItemService;
import io.vexis.polaris.domain.models.dtos.dashboard.DashboardMetricsDTO;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final ShoppingItemService shoppingItemService;

  @GetMapping("/recent-shopping-items")
  public ResponseEntity<List<ShoppingItemDTO>> listRecently() {
    return ResponseEntity.ok(shoppingItemService.listRecently());
  }

  @GetMapping("/metrics")
  public ResponseEntity<DashboardMetricsDTO> getMetrics() {
    return ResponseEntity.ok(
        new DashboardMetricsDTO(
            shoppingItemService.countAll(), shoppingItemService.getTotalPrice()));
  }
}
