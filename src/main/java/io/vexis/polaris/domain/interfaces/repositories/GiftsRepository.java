package io.vexis.polaris.domain.interfaces.repositories;

import io.vexis.polaris.domain.models.entities.Gift;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface GiftsRepository
    extends JpaRepository<Gift, Long>, JpaSpecificationExecutor<Gift> {

        @Query(
            """
            select coalesce(sum(itm.price), 0)
            from Gift itm
        """
        )
        BigDecimal getTotalPrice();
    }
