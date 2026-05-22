package io.vexis.polaris.domain.models.dtos.giftstatus;

import java.time.Instant;

public record GiftStatusDTO(Long id, String name, Instant createdAt, Instant updatedAt) {}
