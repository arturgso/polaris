package io.vexis.polaris.domain.models.dtos.persons;

import jakarta.validation.constraints.NotBlank;

public record NewPersonDTO(
       @NotBlank String name,
       Short birthdayMonth,
       Short birthdayDay
) {
}
