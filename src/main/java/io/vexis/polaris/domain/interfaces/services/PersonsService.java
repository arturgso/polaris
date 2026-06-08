package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import io.vexis.polaris.domain.models.entities.Person;

public interface PersonsService
    extends ListCrudService<NewPersonDTO, UpdatePersonDTO, PersonDTO, Person, Long> {
  PersonDTO getById(Long id);
}
