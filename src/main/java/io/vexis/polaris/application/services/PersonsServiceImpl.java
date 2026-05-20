package io.vexis.polaris.application.services;

import io.vexis.polaris.application.factories.PersonsFactory;
import io.vexis.polaris.domain.interfaces.mappers.PersonsMapper;
import io.vexis.polaris.domain.interfaces.repositories.PersonsRepository;
import io.vexis.polaris.domain.interfaces.services.PersonsService;
import io.vexis.polaris.domain.models.dtos.persons.NewPersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import io.vexis.polaris.domain.models.entities.person.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonsServiceImpl implements PersonsService {

    private final PersonsRepository repository;
    private final PersonsFactory factory;
    private final PersonsMapper mapper;

    @Override
    public PersonDTO create(NewPersonDTO dto) {
        Person person = repository.save(
                factory.create(dto.name(), dto.birthdayDay(), dto.birthdayMonth())
        );

        return mapper.toDTO(person);
    }

    @Override
    public List<PersonDTO> getAll() {
        return List.of();
    }

    @Override
    public void update(UpdatePersonDTO dto, UUID id) {

    }

    @Override
    public void delete(UUID id) {

    }
}
