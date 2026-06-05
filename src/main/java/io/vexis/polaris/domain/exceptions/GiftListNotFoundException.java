package io.vexis.polaris.domain.exceptions;

public class GiftListNotFoundException extends ResourceNotFoundException {

  public GiftListNotFoundException() {
    super("Gift list not found");
  }
}
