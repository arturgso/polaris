package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.GiftList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GiftListRepository extends JpaRepository<GiftList, Long>, JpaSpecificationExecutor<GiftList> {
  List<GiftList> findAllByInVaultTrue();
}
