package io.vexis.polaris.domain.models.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.*;
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

  @ManyToOne
  @JoinColumn(name = "status_id")
  private ShoppingItemStatus status;

  @CreationTimestamp private Instant createdAt;

  @UpdateTimestamp private Instant updatedAt;
}
