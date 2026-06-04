package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.GiftStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftStatusRepository extends JpaRepository<GiftStatus, Long> {

  Optional<GiftStatus> findByTag(String tag);
}
