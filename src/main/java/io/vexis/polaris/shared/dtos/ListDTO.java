package io.vexis.polaris.shared.dtos;

import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public abstract class ListDTO {
    Long id;
    String title;
    Instant createdAt;
    Instant updatedAt;
}