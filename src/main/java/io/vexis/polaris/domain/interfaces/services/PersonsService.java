package io.vexis.polaris.domain.interfaces.services;

import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import io.vexis.polaris.domain.models.entities.Person;
import java.util.List;

public interface PersonsService {
  PersonDTO create(NewPersonDTO dto);

  List<PersonDTO> list();

  PersonDTO getById(Long id);

  Person getEntity(Long personId);

  void update(UpdatePersonDTO dto, Long id);

  void delete(Long id);
}
