package io.vexis.polaris.domain.models.dtos.persons;

public record UpdatePersonDTO(
        String name,
        Short birthdayDay,
        Short birthdayMonth
) {
}
