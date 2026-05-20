package io.vexis.polaris.models.dtos.persons;

import io.vexis.polaris.models.entities.person.Person;

import java.time.Instant;
import java.util.UUID;

public record PersonDTO(
        UUID id,
        String name,
        String birthDay,
        Instant createdAt,
        Instant updatedAt
) {
    public static PersonDTO fromEntity(Person person) {
        String birthday = String.format("%02d/%02d",
                person.getBirthdayDay(),
                person.getBirthdayMonth()
        );

        return new PersonDTO(
                person.getId(),
                person.getName(),
                birthday,
                person.getCreatedAt(),
                person.getUpdatedAt()
        );
    }
}
