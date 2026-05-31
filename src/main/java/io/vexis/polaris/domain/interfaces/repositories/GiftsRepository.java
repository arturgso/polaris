package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.Gift;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftsRepository extends JpaRepository<Gift, Long> {

  List<Gift> findAllByGiftForId(Long personId);
}
