package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.ShoppingItemCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingItemCategoriesRepository
    extends JpaRepository<ShoppingItemCategory, Long> {

  Optional<ShoppingItemCategory> findByName(String name);
}
