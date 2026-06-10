package io.vexis.polaris.domain.models.dtos.shoppinglist.shoppinglist;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.vexis.polaris.domain.models.dtos.shoppinglist.shoppingitem.ShoppingItemDTO;
import io.vexis.polaris.shared.dtos.ListDTO;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id", "title", "inVault", "createdAt", "updatedAt", "items"})
@Data
public class ShoppingListDTO extends ListDTO {

  public List<ShoppingItemDTO> items;
}
