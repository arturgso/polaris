package io.vexis.polaris.domain.exceptions;

public class EventNotFoundException extends ResourceNotFoundException {

  public EventNotFoundException() {
    super("Event not found");
  }
}
