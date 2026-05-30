package io.vexis.polaris.domain.exceptions;

public class PersonNotFoundException extends ResourceNotFoundException {

  public PersonNotFoundException() {
    super("Person not found");
  }
}
