package io.vexis.polaris.domain.models.dtos.shoppinglist.categories;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewCategoryDTO(
    @NotBlank String name,
    @Nullable @Pattern(regexp = "^#(?:[0-9a-fA-F]{3}){1,2}$", message = "Cor hexadecimal inválida")
        String color) {}
