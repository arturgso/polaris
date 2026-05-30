package io.vexis.polaris.domain.exceptions;

public class GiftStatusNotFoundException extends ResourceNotFoundException {

  public GiftStatusNotFoundException() {
    super("Gift status not found");
  }
}
