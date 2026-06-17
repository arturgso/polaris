package io.vexis.polaris.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShoppingItemStatus {
    IDEA(
        "ideia",
        "#A855F7"
    ),
    PLANNED(
        "planejado",
        "#3B82F6"
    ),
    TO_BUY(
        "a comprar",
        "#F59E0B"
    ),
    BOUGHT(
        "comprado",
        "#22C55E"
    ),
    CANCELED(
        "cancelado",
        "#EF4444"
    );

    private final String name;
    private final String color;
}
