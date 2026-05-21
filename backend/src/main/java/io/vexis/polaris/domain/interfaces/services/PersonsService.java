package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import io.vexis.polaris.domain.models.entities.Person;

import java.util.List;
import java.util.UUID;

public interface PersonsService {
    PersonDTO create(NewPersonDTO dto);
    List<PersonDTO> getAll();
    Person getEntity(UUID personId);
    void update(UpdatePersonDTO dto, UUID id);
    void delete(UUID id);
}
