package io.vexis.polaris.application.factories;

import io.vexis.polaris.domain.models.entities.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonsFactory {

  public Person create(String name, Short birthdayDay, Short birthdayMonth) {
    validateBirthday(birthdayDay, birthdayMonth);
    return Person.builder()
        .name(name)
        .birthdayDay(birthdayDay)
        .birthdayMonth(birthdayMonth)
        .build();
  }

  public void validateBirthday(Short birthdayDay, Short birthdayMonth) {
    if ((birthdayDay == null) != (birthdayMonth == null)) {
      throw new IllegalArgumentException("Birthday day and month must be provided together.");
    }

    if (birthdayDay == null) {
      return;
    }

    if (birthdayMonth < 1 || birthdayMonth > 12 || birthdayDay < 1 || birthdayDay > 31) {
      throw new IllegalArgumentException("Birthday day or month is out of range.");
    }

    if (birthdayMonth == 2 && birthdayDay > 29) {
      throw new IllegalArgumentException("Birthday day is invalid for February.");
    }

    if ((birthdayMonth == 4 || birthdayMonth == 6 || birthdayMonth == 9 || birthdayMonth == 11)
        && birthdayDay == 31) {
      throw new IllegalArgumentException("Birthday day is invalid for month.");
    }
  }
}
