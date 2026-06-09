package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {}
