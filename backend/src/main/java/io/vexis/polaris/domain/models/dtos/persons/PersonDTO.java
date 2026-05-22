package io.vexis.polaris.domain.models.dtos.persons;

import java.time.Instant;
import java.util.UUID;

public record PersonDTO(
    UUID id, String name, String birthday, Instant createdAt, Instant updatedAt) {}
