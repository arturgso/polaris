package io.vexis.polaris.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GiftStatus {

    IDEA(
        "ideia",
        "#A855F7"
    ),
    PURCHASED(
        "comprado",
        "#3B82F6"
    ),
    DELIVERED(
        "entregue",
        "#22C55E"
    );

    private final String name;
    private final String color;
}
