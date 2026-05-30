package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.ShoppingItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ShoppingItemRepository
    extends JpaRepository<ShoppingItem, Long>, JpaSpecificationExecutor<ShoppingItem> {
  List<ShoppingItem> findAll();
}
