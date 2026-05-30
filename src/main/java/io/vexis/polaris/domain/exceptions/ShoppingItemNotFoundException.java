package io.vexis.polaris.domain.exceptions;

public class ShoppingItemNotFoundException extends ResourceNotFoundException {

  public ShoppingItemNotFoundException() {
    super("Shopping item not found");
  }
}
