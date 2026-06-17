package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.models.dtos.MetadataDTO;
import io.vexis.polaris.domain.models.dtos.StatusDTO;
import io.vexis.polaris.domain.enums.ShoppingItemStatus;

import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statuses")
public class StatusController {

  @GetMapping
  public ResponseEntity<MetadataDTO> list() {
    var giftStatuses = Arrays.stream(GiftStatus.values())
    .map(status -> new StatusDTO(
      status.name(),
      status.getName(),
      status.getColor()
    )).toList();

    var shoppingItemStatuses = Arrays.stream(ShoppingItemStatus.values())
    .map(status -> new StatusDTO(
      status.name(),
      status.getName(),
      status.getColor()
    )).toList();

    return ResponseEntity.ok(
      new MetadataDTO(
        giftStatuses,
        shoppingItemStatuses
      ));
  }


}
