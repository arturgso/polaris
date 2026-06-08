package io.vexis.polaris.domain.exceptions;

public class ShoppingListNotFoundException extends ResourceNotFoundException {

  public ShoppingListNotFoundException() {
    super("Shopping list not found");
  }
}
