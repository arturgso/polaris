package io.vexis.polaris.domain.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tab_shopping_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String link;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private ShoppingItemCategory category;

  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  private io.vexis.polaris.domain.enums.ShoppingItemStatus  status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "list_id")
  private ShoppingList shoppingList;

  @Builder.Default private Boolean inVault = false;

  @CreationTimestamp private Instant createdAt;

  @UpdateTimestamp private Instant updatedAt;
}
