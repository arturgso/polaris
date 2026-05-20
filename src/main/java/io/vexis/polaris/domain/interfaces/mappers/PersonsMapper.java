package io.vexis.polaris.domain.interfaces.mappers;

import io.vexis.polaris.domain.models.dtos.persons.PersonDTO;
import io.vexis.polaris.domain.models.dtos.persons.UpdatePersonDTO;
import io.vexis.polaris.domain.models.entities.person.Person;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonsMapper {

    @Mapping(target = "birthday", source= ".", qualifiedByName = "formatBirthday")
    PersonDTO toDTO(Person person);
    Person update(UpdatePersonDTO update, @MappingTarget Person person);

    @Named("formatBirthday")
    default String formatBirthday(Person person) {
        if (person.getBirthdayDay() == null || person.getBirthdayMonth() == null) {
            return null;
        }
        return String.format("%02d/%02d",
                person.getBirthdayDay(),
                person.getBirthdayMonth()
        );
    }
}
