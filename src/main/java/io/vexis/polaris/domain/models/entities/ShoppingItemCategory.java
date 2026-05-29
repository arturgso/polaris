package io.vexis.polaris.domain.models.entities;

import io.vexis.polaris.shared.abstracts.ShoppingItemAttribute;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tab_shopping_item_categories")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ShoppingItemCategory extends ShoppingItemAttribute {
}
