package io.vexis.polaris.domain.models.dtos;

import java.util.List;

public record MetadataDTO(List<StatusDTO> giftStatus, List<StatusDTO> shoppingItemsStatus) {}
