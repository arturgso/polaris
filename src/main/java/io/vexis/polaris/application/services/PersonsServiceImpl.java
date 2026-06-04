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
import io.vexis.polaris.shared.ListMapper;
import jakarta.transaction.Transactional;
import java.util.List;
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
  public List<PersonDTO> list() {
    log.debug("Listing persons");
    List<Person> personsList = repository.findAll();
    return ListMapper.createResponseList(personsList, mapper::toDTO);
  }

  public PersonDTO getById(Long id) {
    log.debug("Loading person DTO id={}", id);
    return mapper.toDTO(getEntity(id));
  }

  @Override
  public Person getEntity(Long personId) {
    log.debug("Loading person id={}", personId);
    return repository.findById(personId).orElseThrow(PersonNotFoundException::new);
  }

  @Transactional
  @Override
  public void update(UpdatePersonDTO dto, Long id) {
    log.info("Updating person id={}", id);
    var person = repository.findById(id).orElseThrow(PersonNotFoundException::new);
    var birthdayDay = dto.birthdayDay() == null ? person.getBirthdayDay() : dto.birthdayDay();
    var birthdayMonth =
        dto.birthdayMonth() == null ? person.getBirthdayMonth() : dto.birthdayMonth();
    factory.validateBirthday(birthdayDay, birthdayMonth);
    person = mapper.update(dto, person);

    repository.save(person);
    log.info("Person updated id={}", id);
  }

  @Transactional
  @Override
  public void delete(Long id) {
    log.info("Deleting person id={}", id);
    if (!repository.existsById(id)) {
      throw new PersonNotFoundException();
    }
    repository.deleteById(id);
    log.info("Person deleted id={}", id);
  }
}
