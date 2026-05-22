package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GiftsRepository extends JpaRepository<Gift, UUID> {

    List<Gift> findAllByGiftForId(UUID personId);
}
