package io.vexis.polaris.domain.exceptions;

public class ShoppingItemCategoryNotFoundException extends ResourceNotFoundException {

  public ShoppingItemCategoryNotFoundException() {
    super("Shopping item category not found");
  }
}
