package io.vexis.polaris.models.dtos.persons;

public record UpdatePersonDTO(
        String name,
        Short birthdayDay,
        Short birthdayMonth
) {
}
