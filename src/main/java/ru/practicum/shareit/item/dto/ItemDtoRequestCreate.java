package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class ItemDtoRequestCreate {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Description is required.")
    private String description;

    @NotNull(message = "Available is required")
    private Boolean available;
}
