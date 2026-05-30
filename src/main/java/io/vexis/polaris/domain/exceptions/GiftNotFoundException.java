package io.vexis.polaris.domain.exceptions;

public class GiftNotFoundException extends ResourceNotFoundException {

  public GiftNotFoundException() {
    super("Gift not found");
  }
}
