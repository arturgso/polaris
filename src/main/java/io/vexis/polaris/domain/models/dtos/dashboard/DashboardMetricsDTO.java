package io.vexis.polaris.domain.models.dtos.dashboard;

import java.math.BigDecimal;

public record DashboardMetricsDTO(
        Long shoppingItemsCount,
        BigDecimal shoppingItemsTotal
) {
}
