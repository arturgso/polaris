package io.vexis.polaris.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import io.vexis.polaris.domain.models.entities.GiftList;

public interface GiftListRepository extends JpaRepository<GiftList, Long> {

}