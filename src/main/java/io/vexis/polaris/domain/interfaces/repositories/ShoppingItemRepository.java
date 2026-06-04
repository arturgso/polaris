package io.vexis.polaris.domain.interfaces.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import io.vexis.polaris.domain.models.entities.ShoppingItem;

public interface ShoppingItemRepository
    extends JpaRepository<ShoppingItem, Long>, JpaSpecificationExecutor<ShoppingItem> {
  
      @Query(
      value =
          """
    select * from tab_shopping_items
    order by id
    desc limit 5
    """,
      nativeQuery = true)
  List<ShoppingItem> findRecentlyInserted();

  @Query(
      """
    select coalesce(sum(itm.price), 0)
        from ShoppingItem  itm
    """)
  BigDecimal getTotalPrice();
}
