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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonsServiceImpl implements PersonsService {

  private final PersonsRepository repository;
  private final PersonsFactory factory;
  private final PersonsMapper mapper;

  @Override
  public PersonDTO create(NewPersonDTO dto) {
    log.info("Creating person");
    Person person =
        repository.save(factory.create(dto.name(), dto.birthdayDay(), dto.birthdayMonth()));

    log.info("Person created with id={}", person.getId());
    return mapper.toDTO(person);
  }

  @Override
  public List<PersonDTO> getAll() {
    log.debug("Listing persons");
    List<Person> personsList = repository.findAll();
    List<PersonDTO> response = new ArrayList<>();

    for (Person person : personsList) {
      response.add(mapper.toDTO(person));
    }

    log.debug("Found {} persons", response.size());
    return response;
  }

  @Override
  public Person getEntity(UUID personId) {
    log.debug("Loading person id={}", personId);
    return repository.findById(personId).orElseThrow(PersonNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdatePersonDTO dto, UUID id) {
    log.info("Updating person id={}", id);
    var person = repository.findById(id).orElseThrow(PersonNotFoundException::new);
    person = mapper.update(dto, person);

    repository.save(person);
    log.info("Person updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    log.info("Deleting person id={}", id);
    repository.deleteById(id);
    log.info("Person deleted id={}", id);
  }
}
