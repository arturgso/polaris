package io.vexis.polaris.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.vexis.polaris.domain.models.entities.ShoppingList;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

}