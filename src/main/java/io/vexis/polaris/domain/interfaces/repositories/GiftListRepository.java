package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.GiftList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftListRepository extends JpaRepository<GiftList, Long> {}
