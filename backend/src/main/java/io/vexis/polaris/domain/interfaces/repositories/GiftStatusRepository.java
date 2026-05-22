package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.GiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftStatusRepository extends JpaRepository<GiftStatus, Long> {

    Optional<GiftStatus> findByName(String name);
}
