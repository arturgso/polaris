package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.ShoppingList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoppingListRepository
    extends JpaRepository<ShoppingList, Long>, JpaSpecificationExecutor<ShoppingList> {
  List<ShoppingList> findAllByInVaultTrue();
}
