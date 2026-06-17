package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gift-statuses")
public class GiftStatusController {

  @GetMapping
  public ResponseEntity<Map<String, GiftDTO.Status>> list() {
    Map<String, GiftDTO.Status> statuses = Arrays.stream(GiftStatus.values())
      .collect(Collectors.toMap(Enum::name,
        status -> new GiftDTO.Status(
          status.getName(),
          status.getColor()
        )
      ));

    return ResponseEntity.ok(statuses);
  }
}
