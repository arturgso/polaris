package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonsFactory {

    public Person create(String name, Short birthdayDay ,Short birthdayMonth) {
        return Person.builder()
                .name(name)
                .birthdayDay(birthdayDay)
                .birthdayMonth(birthdayMonth)
                .build();
    }
}
