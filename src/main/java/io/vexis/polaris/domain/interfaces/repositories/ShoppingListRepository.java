package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.ShoppingList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    List<ShoppingList> findAllByInVaultTrue();
}
