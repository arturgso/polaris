package io.vexis.polaris.domain.models.entities;

import io.vexis.polaris.shared.abstracts.ListEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tab_shopping_list")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ShoppingList extends ListEntity {

  @Builder.Default
  @OneToMany(mappedBy = "shoppingList", fetch = FetchType.LAZY)
  private List<ShoppingItem> items = new ArrayList<>();
}
