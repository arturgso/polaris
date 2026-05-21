package io.vexis.polaris.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.vexis.polaris.domain.interfaces.services.GiftsService;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.domain.models.dtos.gifts.NewGiftDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/gifts")
@RequiredArgsConstructor
public class GiftsController {

    private final GiftsService service;

    @PostMapping
    public ResponseEntity<GiftDTO> create(@RequestBody @Valid NewGiftDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }
    
}
