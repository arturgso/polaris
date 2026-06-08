package io.vexis.polaris.domain.models.dtos.giftlist;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.vexis.polaris.domain.models.dtos.gifts.GiftDTO;
import io.vexis.polaris.shared.dtos.ListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "title",
        "createdAt",
        "updatedAt",
        "gifs"
})
@EqualsAndHashCode(callSuper = true)
@Data
public class GiftListDTO extends ListDTO {
    List<GiftDTO> gifts;

}