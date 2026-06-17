package io.vexis.polaris.application.controllers;

import io.vexis.polaris.domain.enums.GiftStatus;
import io.vexis.polaris.domain.interfaces.services.GiftStatusService;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.GiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.NewGiftStatusDTO;
import io.vexis.polaris.domain.models.dtos.giftstatus.UpdateGiftStatusDTO;
import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
