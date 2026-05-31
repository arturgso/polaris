package io.vexis.polaris.domain.models.entities;

import io.vexis.polaris.shared.abstracts.ShoppingItemAttribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tab_gift_statuses")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class GiftStatus extends ShoppingItemAttribute {}
