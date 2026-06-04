package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.ShoppingItemStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingItemStatusesRepository extends JpaRepository<ShoppingItemStatus, Long> {

  Optional<ShoppingItemStatus> findByTag(String tag);
}
