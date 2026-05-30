package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.PersonsFactory;
import io.vexis.polaris.domain.exceptions.PersonNotFoundException;
import io.vexis.polaris.domain.interfaces.mappers.PersonsMapper;
import io.vexis.polaris.domain.interfaces.repositories.PersonsRepository;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import io.vexis.polaris.domain.models.entities.Person;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonsServiceImpl implements PersonsService {

  private final PersonsRepository repository;
  private final PersonsFactory factory;
  private final PersonsMapper mapper;

  @Override
  public PersonDTO create(NewPersonDTO dto) {
    Person person =
        repository.save(factory.create(dto.name(), dto.birthdayDay(), dto.birthdayMonth()));

    return mapper.toDTO(person);
  }

  @Override
  public List<PersonDTO> getAll() {
    List<Person> personsList = repository.findAll();
    List<PersonDTO> response = new ArrayList<>();

    for (Person person : personsList) {
      response.add(mapper.toDTO(person));
    }

    return response;
  }

  @Override
  public Person getEntity(UUID personId) {
    return repository.findById(personId).orElseThrow(PersonNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdatePersonDTO dto, UUID id) {
    var person = repository.findById(id).orElseThrow(PersonNotFoundException::new);
    person = mapper.update(dto, person);

    repository.save(person);
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    repository.deleteById(id);
  }
}
