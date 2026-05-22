package io.vexis.polaris.domain.models.dtos.events;

import java.time.Instant;

public record EventDTO(Long id, String name, Instant createdAt, Instant updatedAt) {}
