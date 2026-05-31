package io.vexis.polaris.domain.models.dtos.persons;

import java.time.Instant;

public record PersonDTO(
    Long id,
    String name,
    Short birthdayMonth,
    Short birthdayDay,
    String birthday,
    Instant createdAt,
    Instant updatedAt) {}
